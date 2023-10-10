package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.ProjectAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.project.Project;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.*;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output.OutputKafkaProducerProjectService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output.OutputProjectService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output.OutputRemoteApiCompanyService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output.OutputRemoteApiEmployeeService;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.mappers.Mapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.models.ProjectDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
class UseCaseTest {
    @Mock
    private OutputKafkaProducerProjectService kafkaProducerService;
    @Mock
    private OutputProjectService outputProjectService;
    @Mock
    private OutputRemoteApiEmployeeService outputRemoteAPIEmployeeService;
    @Mock
    private OutputRemoteApiCompanyService outputRemoteAPICompanyService;
    @InjectMocks
    private UseCase underTest;
    private static final String PROJECT_ID = "project-id";
    private ProjectDto projectDto;
    private Project project;
    private static final String EMPLOYEE_ID = "employee-id";
    private Employee employee;
    private static final String COMPANY_ID = "company-id";
    private Company company;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectDto = ProjectDto.builder()
                .name("Guppy")
                .description("outil d'aide au business analyst (BA) à la rédaction des us")
                .priority(1)
                .state("ongoing")
                .employeeId(EMPLOYEE_ID)
                .companyId(COMPANY_ID)
                .build();
        employee = new Employee(EMPLOYEE_ID, "Placide", "Nduwayo", "placide.nduwayo@natan.fr", "2020-10-27:00:00:00",
                "active", "software-engineer");
        company = new Company(COMPANY_ID, "Natan", "Paris", "esn", "company-creation");
        project = Mapper.fromTo(projectDto);
        project.setProjectId(PROJECT_ID);
        project.setCreatedDate(Timestamp.from(Instant.now()).toString());
        project.setEmployeeId(EMPLOYEE_ID);
        project.setEmployee(employee);
        project.setCompanyId(COMPANY_ID);
        project.setCompany(company);
    }

    @Test
    void produceKafkaEventProjectCreate() throws RemoteCompanyApiException, ProjectPriorityInvalidException, ProjectAlreadyExistsException,
            RemoteEmployeeApiException, ProjectStateInvalidException, ProjectFieldsEmptyException {
        //PREPARE
        ProjectAvro projectAvro = Mapper.fromBeanToAvro(project);
        //EXECUTE
        Mockito.when(outputRemoteAPIEmployeeService.getRemoteEmployeeAPI(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
        Mockito.when(outputRemoteAPICompanyService.getRemoteCompanyAPI(COMPANY_ID)).thenReturn(Optional.of(company));
        Mockito.when(kafkaProducerService.produceKafkaEventProjectCreate(Mockito.any(ProjectAvro.class))).thenReturn(projectAvro);
        Project actual = underTest.produceKafkaEventProjectCreate(projectDto);
        //VERIFY
        Assertions.assertAll("gpe of assertions", () -> {
            Mockito.verify(outputRemoteAPIEmployeeService, Mockito.atLeast(1)).getRemoteEmployeeAPI(EMPLOYEE_ID);
            Mockito.verify(outputRemoteAPICompanyService, Mockito.atLeast(1)).getRemoteCompanyAPI(COMPANY_ID);
            Mockito.verify(kafkaProducerService, Mockito.atLeast(1)).produceKafkaEventProjectCreate(Mockito.any());
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(projectAvro.getProjectId(),actual.getProjectId());
            Assertions.assertEquals(projectAvro.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
            Assertions.assertEquals(projectAvro.getEmployee().getFirstname(), actual.getEmployee().getFirstname());
            Assertions.assertEquals(projectAvro.getEmployee().getLastname(), actual.getEmployee().getLastname());
            Assertions.assertEquals(projectAvro.getCompany().getCompanyId(), actual.getCompany().getCompanyId());
            Assertions.assertEquals(projectAvro.getCompany().getName(), actual.getCompany().getName());
            Assertions.assertEquals(projectAvro.getCompany().getAgency(), actual.getCompany().getAgency());
        });
    }

    @Test
    void createProject() {
        //PREPARE
        Project bean = Mapper.fromTo(projectDto);
        //EXECUTE
        Mockito.when(outputProjectService.saveProject(Mockito.any(Project.class))).thenReturn(bean);
        Project saved = underTest.createProject(bean);
        //VERIFY
        Assertions.assertAll("gpe of assertions",()->{
            Mockito.verify(outputProjectService, Mockito.atLeast(1)).saveProject(bean);
            Assertions.assertEquals(bean, saved);
        });
    }

    @Test
    void getProject() throws ProjectNotFoundException {
        //PREPARE
        Project bean = Mapper.fromTo(projectDto);
        //EXECUTE
        Mockito.when(outputProjectService.getProject(PROJECT_ID)).thenReturn(Optional.of(bean));
        Project actual = underTest.getProject(PROJECT_ID).orElseThrow(ProjectNotFoundException::new);
        //VERIFY
        Assertions.assertAll("gpe of assertions",()->{
            Mockito.verify(outputProjectService, Mockito.atLeast(1)).getProject(PROJECT_ID);
            Assertions.assertEquals(bean, actual);
        });
    }

    @Test
    void loadProjectByInfo() {
        //PREPARE
        String name = "Guppy";
        String description = "description";
        String state = "ongoing";
        List<Project> projects = List.of(Mapper.fromTo(projectDto));
        //EXECUTE
        Mockito.when(outputProjectService.loadProjectByInfo(name, description, state, EMPLOYEE_ID, COMPANY_ID)).thenReturn(projects);
        List<Project> lists = underTest.loadProjectByInfo(name, description, state, EMPLOYEE_ID, COMPANY_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions",()->{
            Mockito.verify(outputProjectService, Mockito.atLeast(1))
                    .loadProjectByInfo(name, description, state, EMPLOYEE_ID, COMPANY_ID);
            Assertions.assertEquals(projects.size(), lists.size());
        });
    }

    @Test
    void produceKafkaEventProjectDelete() throws ProjectNotFoundException, RemoteCompanyApiException, RemoteEmployeeApiException {
        //PREPARE
        ProjectAvro projectAvro = Mapper.fromBeanToAvro(project);
        //EXECUTE
        Mockito.when(outputProjectService.getProject(PROJECT_ID)).thenReturn(Optional.of(project));
        Mockito.when(outputRemoteAPIEmployeeService.getRemoteEmployeeAPI(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
        Mockito.when(outputRemoteAPICompanyService.getRemoteCompanyAPI(COMPANY_ID)).thenReturn(Optional.of(company));
        Mockito.when(kafkaProducerService.produceKafkaEventProjectDelete(projectAvro)).thenReturn(projectAvro);
        Project actual = underTest.produceKafkaEventProjectDelete(PROJECT_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions", () -> {
            Mockito.verify(outputProjectService, Mockito.atLeast(1))
                    .getProject(PROJECT_ID);
            Mockito.verify(kafkaProducerService, Mockito.atLeast(1))
                    .produceKafkaEventProjectDelete(projectAvro);
            Assertions.assertEquals(projectAvro.getProjectId(),actual.getProjectId());
            Assertions.assertEquals(projectAvro.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
            Assertions.assertEquals(projectAvro.getEmployee().getFirstname(), actual.getEmployee().getFirstname());
            Assertions.assertEquals(projectAvro.getEmployee().getLastname(), actual.getEmployee().getLastname());
            Assertions.assertEquals(projectAvro.getCompany().getCompanyId(), actual.getCompany().getCompanyId());
            Assertions.assertEquals(projectAvro.getCompany().getName(), actual.getCompany().getName());
            Assertions.assertEquals(projectAvro.getCompany().getAgency(), actual.getCompany().getAgency());
            Mockito.verify(outputRemoteAPIEmployeeService, Mockito.atLeast(1))
                    .getRemoteEmployeeAPI(EMPLOYEE_ID);
            Mockito.verify(outputRemoteAPICompanyService, Mockito.atLeast(1))
                    .getRemoteCompanyAPI(COMPANY_ID);
        });
    }

    @Test
    void deleteProject() throws ProjectNotFoundException {
        //PREPARE
        Project bean = Mapper.fromTo(projectDto);
        //EXECUTE
        Mockito.when(outputProjectService.getProject(PROJECT_ID)).thenReturn(Optional.of(bean));
        Project obtained = underTest.getProject(PROJECT_ID).orElseThrow(ProjectNotFoundException::new);
        Mockito.when(outputProjectService.deleteProject(obtained.getProjectId())).thenReturn("");
        String msg = underTest.deleteProject(PROJECT_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(outputProjectService, Mockito.atLeast(1))
                        .deleteProject(obtained.getProjectId()),
                () -> Assertions.assertEquals("Project" + obtained + "successfully deleted", msg),
                () -> Assertions.assertEquals(bean, obtained));
    }


    @Test
    void produceKafkaEventProjectUpdate() throws ProjectNotFoundException, RemoteCompanyApiException, RemoteEmployeeApiException,
            ProjectPriorityInvalidException, ProjectStateInvalidException, ProjectFieldsEmptyException {
        //PREPARE
        ProjectAvro projectAvro = Mapper.fromBeanToAvro(project);
        log.info("{}",projectAvro.toString());
        //EXECUTE
        Mockito.when(outputRemoteAPIEmployeeService.getRemoteEmployeeAPI(EMPLOYEE_ID))
                .thenReturn(Optional.of(employee));
        Mockito.when(outputRemoteAPICompanyService.getRemoteCompanyAPI(COMPANY_ID))
                .thenReturn(Optional.of(company));
        Mockito.when(outputProjectService.getProject(PROJECT_ID)).thenReturn(Optional.of(project));
        Mockito.when(kafkaProducerService.produceKafkaEventProjectEdit(Mockito.any(ProjectAvro.class)))
                .thenReturn(projectAvro);
        Project actual = underTest.produceKafkaEventProjectUpdate(projectDto, PROJECT_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions",()->{
            Mockito.verify(outputRemoteAPIEmployeeService, Mockito.atLeast(1))
                    .getRemoteEmployeeAPI(projectDto.getEmployeeId());
            Mockito.verify(outputRemoteAPICompanyService, Mockito.atLeast(1))
                    .getRemoteCompanyAPI(projectDto.getCompanyId());
            Mockito.verify(outputProjectService, Mockito.atLeast(1)).getProject(PROJECT_ID);
            Mockito.verify(kafkaProducerService, Mockito.atLeast(1))
                    .produceKafkaEventProjectEdit(Mockito.any(ProjectAvro.class));
            Assertions.assertNotNull(actual);
        });
    }

    @Test
    void updateProject() {
        //PREPARE
        Project bean = Mapper.fromTo(projectDto);
        //EXECUTE
        Mockito.when(outputProjectService.updateProject(bean)).thenReturn(new Project());
        Project updated = underTest.updateProject(bean);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(outputProjectService, Mockito.atLeast(1)).updateProject(bean),
                () -> Assertions.assertNotNull(updated),
                () -> Assertions.assertNotEquals(projectDto.getDescription(), updated.getDescription()));
    }

    @Test
    void loadProjectsAssignedToEmployee() throws RemoteEmployeeApiException {
        //PREPARE
        List<Project> beans = List.of(Mapper.fromTo(projectDto));
        //EXECUTE
        Mockito.when(outputRemoteAPIEmployeeService.getRemoteEmployeeAPI(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
        Employee obtained = underTest.getRemoteEmployeeAPI(EMPLOYEE_ID).orElseThrow(RemoteEmployeeApiException::new);
        Mockito.when(outputProjectService.loadProjectsAssignedToEmployee(EMPLOYEE_ID)).thenReturn(beans);
        List<Project> projects = underTest.loadProjectsAssignedToEmployee(EMPLOYEE_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(outputRemoteAPIEmployeeService, Mockito.atLeast(1)).getRemoteEmployeeAPI(EMPLOYEE_ID),
                () -> Mockito.verify(outputProjectService, Mockito.atLeast(1)).loadProjectsAssignedToEmployee(EMPLOYEE_ID),
                () -> Assertions.assertEquals(beans.get(0), projects.get(0)),
                () -> Assertions.assertEquals(beans.size(), projects.size()),
                () -> Assertions.assertEquals(employee, obtained));
    }

    @Test
    void loadProjectsOfCompanyC() throws RemoteCompanyApiException {
        //PREPARE
        List<Project> beans = List.of(Mapper.fromTo(projectDto));
        //EXECUTE
        Mockito.when(outputRemoteAPICompanyService.getRemoteCompanyAPI(COMPANY_ID)).thenReturn(Optional.of(company));
        Company actual = underTest.getRemoteApiCompany(COMPANY_ID).orElseThrow(RemoteCompanyApiException::new);
        Mockito.when(outputProjectService.loadProjectsOfCompanyC(actual.getCompanyId())).thenReturn(beans);
        List<Project> actuals = underTest.loadProjectsOfCompanyC(COMPANY_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(outputRemoteAPICompanyService, Mockito.atLeast(1)).getRemoteCompanyAPI(COMPANY_ID),
                () -> Mockito.verify(outputProjectService, Mockito.atLeast(1)).loadProjectsOfCompanyC(actual.getCompanyId()),
                () -> Assertions.assertEquals(company, actual),
                () -> Assertions.assertFalse(actuals.isEmpty()));
    }

    @Test
    void getAllProjects() {
        //PREPARE
        List<Project> beans = List.of(Mapper.fromTo(projectDto));
        //EXECUTE
        Mockito.when(outputProjectService.getAllProjects()).thenReturn(beans);
        List<Project> actuals = underTest.getAllProjects();
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(outputProjectService, Mockito.atLeast(1)).getAllProjects(),
                () -> Assertions.assertEquals(beans.size(), actuals.size()));
    }

    @Test
    void getRemoteCompanyAPI() throws RemoteCompanyApiException {
        //PREPARE
        //EXECUTE
        Mockito.when(outputRemoteAPICompanyService.getRemoteCompanyAPI(COMPANY_ID)).thenReturn(Optional.of(company));
        Company actual = underTest.getRemoteApiCompany(COMPANY_ID).orElseThrow(RemoteCompanyApiException::new);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(outputRemoteAPICompanyService, Mockito.atLeast(1)).getRemoteCompanyAPI(COMPANY_ID),
                () -> Assertions.assertEquals(company, actual));
    }

    @Test
    void getRemoteEmployeeAPI() throws RemoteEmployeeApiException {
        //PREPARE
        //EXECUTE
        Mockito.when(outputRemoteAPIEmployeeService.getRemoteEmployeeAPI(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
        Employee actual = underTest.getRemoteEmployeeAPI(EMPLOYEE_ID).orElseThrow(RemoteEmployeeApiException::new);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(outputRemoteAPIEmployeeService, Mockito.atLeast(1)).getRemoteEmployeeAPI(EMPLOYEE_ID),
                () -> Assertions.assertEquals(employee, actual));
    }
}