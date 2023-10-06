package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.ProjectAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.project.Project;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.input.InputProjectService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.input.InputRemoteApiCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.input.InputRemoteApiEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output.OutputKafkaProducerProjectService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output.OutputProjectService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output.OutputRemoteApiCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output.OutputRemoteApiEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.mappers.Mapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.models.ProjectDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UseCase implements InputProjectService, InputRemoteApiEmployeeService, InputRemoteApiCompanyService {
    private final OutputKafkaProducerProjectService kafkaProducerService;
    private final OutputProjectService outputProjectService;
    private final OutputRemoteApiEmployeeService outputEmployeeAPIService;
    private final OutputRemoteApiCompanyService outputCompanyAPIService;

    public UseCase(OutputKafkaProducerProjectService kafkaProducerService, OutputProjectService outputProjectService, OutputRemoteApiEmployeeService outputEmployeeAPIService, OutputRemoteApiCompanyService outputCompanyAPIService) {
        this.kafkaProducerService = kafkaProducerService;
        this.outputProjectService = outputProjectService;
        this.outputEmployeeAPIService = outputEmployeeAPIService;
        this.outputCompanyAPIService = outputCompanyAPIService;
    }

    private void checkProjectValidity(String name, String desc, int priority, String state, String employeeId, String companyId) throws
            ProjectFieldsEmptyException, ProjectPriorityInvalidException, ProjectStateInvalidException, RemoteEmployeeApiException, RemoteCompanyApiException {
        if (!Validator.isValidProject(name, desc, employeeId, companyId)) {
            throw new ProjectFieldsEmptyException();
        } else if (!Validator.isValidProject(priority)) {
            throw new ProjectPriorityInvalidException();
        } else if (!Validator.isValidProject(state)) {
            throw new ProjectStateInvalidException();
        }
        Employee employee = getRemoteEmployeeAPI(employeeId).orElseThrow(RemoteEmployeeApiException::new);
        if (Validator.remoteEmployeeApiUnreachable(employee.getEmployeeId())) {
            throw new RemoteEmployeeApiException();
        }
        Company company = getRemoteApiCompany(companyId).orElseThrow(RemoteCompanyApiException::new);
        if (Validator.remoteCompanyApiUnreachable(company.getCompanyId())) {
            throw new RemoteCompanyApiException();
        }
    }

    private void checkProjectAlreadyExists(String name, String desc, String state, String employeeId, String companyId) throws
            ProjectAlreadyExistsException {
        if (!loadProjectByInfo(name, desc, state, employeeId, companyId).isEmpty()) {
            throw new ProjectAlreadyExistsException();
        }
    }

    @Override
    public Project produceKafkaEventProjectCreate(ProjectDto projectDto) throws ProjectAlreadyExistsException,
            ProjectPriorityInvalidException, ProjectStateInvalidException, RemoteEmployeeApiException, RemoteCompanyApiException,
            ProjectFieldsEmptyException {

        Validator.format(projectDto);
        checkProjectValidity(projectDto.getName(), projectDto.getDescription(), projectDto.getPriority(), projectDto.getState(),
                projectDto.getEmployeeId(), projectDto.getCompanyId());
        checkProjectAlreadyExists(projectDto.getName(), projectDto.getDescription(), projectDto.getState(), projectDto.getEmployeeId(),
                projectDto.getCompanyId());
        Project project = Mapper.fromTo(projectDto);
        project.setProjectId(UUID.randomUUID().toString());
        project.setCreatedDate(Timestamp.from(Instant.now()).toString());
        Employee employee = getRemoteEmployeeAPI(projectDto.getEmployeeId()).orElseThrow(RemoteEmployeeApiException::new);
        Company company = getRemoteApiCompany(projectDto.getCompanyId()).orElseThrow(RemoteCompanyApiException::new);
        project.setEmployee(employee);
        project.setCompany(company);
        ProjectAvro projectAvro = Mapper.fromBeanToAvro(project);
        return Mapper.fromAvroToBean(kafkaProducerService.produceKafkaEventProjectCreate(projectAvro));
    }

    @Override
    public Project createProject(Project project) {
        return outputProjectService.saveProject(project);
    }

    @Override
    public Optional<Project> getProject(String projectId) throws ProjectNotFoundException {
        return Optional.of(outputProjectService.getProject(projectId).orElseThrow(ProjectNotFoundException::new));
    }

    @Override
    public List<Project> loadProjectByInfo(String name, String desc, String state, String employeeId, String companyId) {
        return outputProjectService.loadProjectByInfo(name, desc, state, employeeId, companyId);
    }

    @Override
    public Project produceKafkaEventProjectDelete(String projectId) throws ProjectNotFoundException {
        Project project = getProject(projectId).orElseThrow(ProjectNotFoundException::new);
        ProjectAvro projectAvro = Mapper.fromBeanToAvro(project);
        return Mapper.fromAvroToBean(kafkaProducerService.produceKafkaEventProjectDelete(projectAvro));
    }

    @Override
    public String deleteProject(String projectId) throws ProjectNotFoundException {
        Project project = getProject(projectId).orElseThrow(ProjectNotFoundException::new);
        outputProjectService.deleteProject(project.getProjectId());
        return "Project" + project + "successfully deleted";
    }

    @Override
    public Project produceKafkaEventProjectUpdate(ProjectDto projectDto, String projectId) throws ProjectNotFoundException,
            ProjectPriorityInvalidException, ProjectStateInvalidException, RemoteEmployeeApiException, RemoteCompanyApiException,
            ProjectFieldsEmptyException {

        Validator.format(projectDto);
        checkProjectValidity(projectDto.getName(), projectDto.getDescription(), projectDto.getPriority(), projectDto.getState(),
                projectDto.getEmployeeId(), projectDto.getCompanyId());
        Project project = getProject(projectId).orElseThrow(ProjectNotFoundException::new);
        ProjectAvro projectAvro = Mapper.fromBeanToAvro(project);

        return Mapper.fromAvroToBean(kafkaProducerService.produceKafkaEventProjectEdit(projectAvro));
    }

    @Override
    public Project updateProject(Project payload) {
        return outputProjectService.updateProject(payload);
    }

    @Override
    public List<Project> loadProjectsAssignedToEmployee(String employeeId) throws RemoteEmployeeApiException {
        Employee employee = getRemoteEmployeeAPI(employeeId).orElseThrow(RemoteEmployeeApiException::new);
        return outputProjectService.loadProjectsAssignedToEmployee(employee.getEmployeeId());
    }

    @Override
    public List<Project> loadProjectsOfCompanyC(String companyId) throws RemoteCompanyApiException {
        Company company = getRemoteApiCompany(companyId).orElseThrow(RemoteCompanyApiException::new);
        return outputProjectService.loadProjectsOfCompanyC(company.getCompanyId());
    }

    @Override
    public List<Project> getAllProjects() {
        return outputProjectService.getAllProjects();
    }

    @Override
    public Optional<Company> getRemoteApiCompany(String companyId) throws RemoteCompanyApiException {
        Company company = outputCompanyAPIService.getRemoteCompanyAPI(companyId).orElseThrow(RemoteCompanyApiException::new);
        return Optional.of(company);
    }

    @Override
    public Optional<Employee> getRemoteEmployeeAPI(String employeeId) throws RemoteEmployeeApiException {
        Employee employee = outputEmployeeAPIService.getRemoteEmployeeAPI(employeeId).orElseThrow(RemoteEmployeeApiException::new);
        return Optional.of(employee);
    }
}