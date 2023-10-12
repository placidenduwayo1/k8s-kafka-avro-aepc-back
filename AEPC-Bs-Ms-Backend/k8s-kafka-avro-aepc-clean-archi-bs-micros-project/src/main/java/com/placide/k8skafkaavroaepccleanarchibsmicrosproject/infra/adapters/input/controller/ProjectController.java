package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.controller;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.project.Project;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.input.InputProjectService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.input.InputRemoteApiCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.input.InputRemoteApiEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.models.ProjectDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {
    private final InputProjectService inputProjectService;
    private final InputRemoteApiEmployeeService inputRemoteApiEmployeeService;
    private final InputRemoteApiCompanyService inputRemoteApiCompanyService;

    public ProjectController(InputProjectService inputProjectService, InputRemoteApiEmployeeService inputRemoteApiEmployeeService,
                             InputRemoteApiCompanyService inputRemoteApiCompanyService) {
        this.inputProjectService = inputProjectService;
        this.inputRemoteApiEmployeeService = inputRemoteApiEmployeeService;
        this.inputRemoteApiCompanyService = inputRemoteApiCompanyService;
    }

    @PostMapping(value = "/projects")
    public List<String> produceConsumeAndSave(@RequestBody ProjectDto dto) throws RemoteCompanyApiException,
            ProjectPriorityInvalidException, ProjectAlreadyExistsException, RemoteEmployeeApiException, ProjectStateInvalidException,
            ProjectFieldsEmptyException {
        Project consumed = inputProjectService.produceKafkaEventProjectCreate(dto);
        Project saved = inputProjectService.createProject(consumed);
        return List.of("consumed: "+consumed,"saved: "+saved);
    }
    @GetMapping(value = "/projects")
    public List<Project> getAllProjects(){
        return setProjectDependency(inputProjectService.getAllProjects());
    }
    @GetMapping(value = "/projects/{id}")
    public Project getProject(@PathVariable(name = "id") String id) throws ProjectNotFoundException {
        return inputProjectService.getProject(id).orElseThrow(ProjectNotFoundException::new);
    }
    @GetMapping(value = "/projects/employees/{employeeId}")
    public List<Project> getProjectsByEmployee(@PathVariable(name = "employeeId") String employeeId) throws RemoteEmployeeApiException {
        List<Project> projects = inputProjectService.loadProjectsAssignedToEmployee(employeeId);
        return setProjectDependency(projects);
    }
    @GetMapping(value = "/projects/companies/{companyId}")
    public List<Project> getProjectsByCompany(@PathVariable(name = "companyId") String companyId) throws RemoteCompanyApiException {
        List<Project> projects = inputProjectService.loadProjectsOfCompanyC(companyId);
        return setProjectDependency(projects);
    }
    @DeleteMapping(value = "/projects/{id}")
    public ResponseEntity<Object> delete(@PathVariable(name = "id") String id) throws ProjectNotFoundException,
            RemoteCompanyApiException, RemoteEmployeeApiException {
        Project consumed = inputProjectService.produceKafkaEventProjectDelete(id);
        inputProjectService.deleteProject(consumed.getProjectId());
        return new ResponseEntity<>(String
                .format("<%s> deleted from db", consumed),
                HttpStatus.OK);
    }
    @PutMapping(value = "/projects/{id}")
    public List<String> update(@PathVariable(name = "id") String id, @RequestBody ProjectDto dto) throws ProjectNotFoundException,
            RemoteCompanyApiException, ProjectPriorityInvalidException, RemoteEmployeeApiException, ProjectStateInvalidException,
            ProjectFieldsEmptyException {
        Project consumed = inputProjectService.produceKafkaEventProjectUpdate(dto, id);
        Project saved = inputProjectService.updateProject(consumed);
        return List.of("consumed: "+consumed,"saved: "+saved);
    }
    private List<Project> setProjectDependency(List<Project> projects){
        projects.forEach((var project)->{
            try {
                Employee remoteEmployee = inputRemoteApiEmployeeService.getRemoteEmployeeAPI(project.getEmployeeId())
                        .orElseThrow(RemoteEmployeeApiException::new);
                Company remoteCompany = inputRemoteApiCompanyService.getRemoteApiCompany(project.getCompanyId())
                        .orElseThrow(RemoteCompanyApiException::new);
                project.setEmployee(remoteEmployee);
                project.setCompany(remoteCompany);
            } catch (RemoteEmployeeApiException | RemoteCompanyApiException e) {
                e.getMessage();
            }
        });
        return projects;
    }
}
