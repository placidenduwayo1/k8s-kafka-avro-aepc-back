package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.services;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.ProjectAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.project.Project;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.ProjectNotFoundException;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.models.CompanyModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.models.EmployeeModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.proxies.CompanyServiceProxy;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.proxies.EmployeeServiceProxy;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.mappers.Mapper;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.models.ProjectModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
class OutputProjectServiceImplTest {
    @Mock
    private ProjectRepository repository;
    @Mock
    private EmployeeServiceProxy employeeServiceProxy;
    @Mock
    private CompanyServiceProxy companyServiceProxy;
    @InjectMocks
    private OutputProjectServiceImpl underTest;
    private static final String TOPIC1 = "topic1";
    private static final String TOPIC2 = "topic2";
    private static final String TOPIC3 = "topic3";
    private static final String PROJECT_ID = "project-id";
    private Project project;
    private static final String EMPLOYEE_ID = "employee-id";
    private Employee employee;
    private static final String COMPANY_ID = "company-id";
    private Company company;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee(EMPLOYEE_ID, "Placide", "Nduwayo", "placide.nduwayo@natan.fr", "2020-10-27:00:00:00",
                "active", "software-engineer");
        company = new Company(COMPANY_ID, "Natan", "Paris", "esn", "company-creation");
        project = new Project(PROJECT_ID, "Guppy", "outil d'aide au business analyst (BA) à la rédaction des us",
                1, "ongoing", Instant.now().toString(), EMPLOYEE_ID, employee, COMPANY_ID, company);
    }

    @Test
    void consumeKafkaEventProjectCreate() {
        //PREPARE
        ProjectAvro projectAvro = Mapper.fromBeanToAvro(project);
        //EXECUTE
        Project consumed = underTest.consumeKafkaEventProjectCreate(projectAvro, TOPIC1);
        log.info("{}", consumed);
        //VERIFY
        Assertions.assertNotNull(consumed);

    }

    @Test
    void saveProject() {
        //PREPARE
        ProjectModel model = Mapper.fromTo(project);
        //EXECUTE
        Mockito.when(repository.save(Mockito.any(ProjectModel.class))).thenReturn(model);
        Project saved = underTest.saveProject(project);
        //VERIFY
        Assertions.assertAll("gpe of assertions", () -> {
            Mockito.verify(repository, Mockito.atLeast(1)).save(Mockito.any());
            Assertions.assertNotNull(saved);
            Assertions.assertEquals(project.getProjectId(), saved.getProjectId());
            Assertions.assertEquals(project.getEmployee().getEmployeeId(), saved.getEmployee().getEmployeeId());
            Assertions.assertEquals(project.getEmployee().toString(), saved.getEmployee().toString());
            Assertions.assertEquals(project.getCompany().toString(), saved.getCompany().toString());
        });
    }

    @Test
    void getProject() throws ProjectNotFoundException {
        //PREPARE
        ProjectModel pModel = Mapper.fromTo(project);
        //EXECUTE
        Mockito.when(repository.findById(PROJECT_ID)).thenReturn(Optional.of(pModel));
        Project pBean = underTest.getProject(PROJECT_ID).orElseThrow(ProjectNotFoundException::new);
        //VERIFY
        Assertions.assertAll("gpe of assertions", () -> {
            Mockito.verify(repository, Mockito.atLeast(1)).findById(PROJECT_ID);
            Assertions.assertNotNull(pBean);
            Assertions.assertEquals(project.getEmployee().toString(), pBean.getEmployee().toString());
            Assertions.assertEquals(project.getCompany().toString(), pBean.getCompany().toString());
        });
    }

    @Test
    void loadProjectByInfo() {
        //PREPARE
        String name = "Guppy";
        String description = "description";
        String state = "ongoing";
        ProjectModel pModel = Mapper.fromTo(project);
        //EXECUTE
        Mockito.when(repository.findByNameAndDescriptionAndStateAndEmployeeIdAndCompanyId(name, description, state, EMPLOYEE_ID, COMPANY_ID))
                .thenReturn(List.of(pModel));
        List<Project> pBeans = underTest.loadProjectByInfo(name, description, state, EMPLOYEE_ID, COMPANY_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions", () -> {
            Mockito.verify(repository).findByNameAndDescriptionAndStateAndEmployeeIdAndCompanyId(name, description, state, EMPLOYEE_ID, COMPANY_ID);
            Assertions.assertEquals(1, pBeans.size());
        });
    }

    @Test
    void consumeKafkaEventProjectDelete() {
        //PREPARE
        ProjectAvro projectAvro = Mapper.fromBeanToAvro(project);
        //EXECUTE
        Project consumed = underTest.consumeKafkaEventProjectDelete(projectAvro, TOPIC2);
        //VERIFY
        Assertions.assertAll("assertions",()->{
            Assertions.assertEquals(project.getProjectId(), consumed.getProjectId());
            Assertions.assertEquals(project.getName(), consumed.getName());
            Assertions.assertEquals(project.getDescription(), consumed.getDescription());
            Assertions.assertEquals(project.getEmployee().toString(), consumed.getEmployee().toString());
        });
    }

    @Test
    void deleteProject() throws ProjectNotFoundException {
        //PREPARE
        ProjectModel pModel = Mapper.fromTo(project);
        //EXECUTE
        Mockito.when(repository.findById(PROJECT_ID)).thenReturn(Optional.of(pModel));
        Project actual1 = underTest.getProject(PROJECT_ID).orElseThrow(ProjectNotFoundException::new);
        ProjectAvro avro = Mapper.fromBeanToAvro(actual1);
        Project consumed = underTest.consumeKafkaEventProjectDelete(avro, TOPIC2);
        String msg = underTest.deleteProject(consumed.getProjectId());
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Assertions.assertNotNull(msg),
                () -> Assertions.assertEquals("project <" + consumed + "> is deleted", msg),
                () -> Mockito.verify(repository).deleteById(PROJECT_ID));
    }

    @Test
    void consumeKafkaEventProjectUpdate() {
        //PREPARE
        ProjectAvro avro = Mapper.fromBeanToAvro(project);
        //EXECUTE
        Project consumed = underTest.consumeKafkaEventProjectUpdate(avro, TOPIC3);
        //VERIFY
        Assertions.assertAll("gpe of assertions", () -> {
            Assertions.assertNotNull(consumed);
            Assertions.assertEquals("Natan", consumed.getCompany().getName());
            Assertions.assertEquals("Placide", consumed.getEmployee().getFirstname());
        });
    }

    @Test
    void updateProject() throws ProjectNotFoundException {
        //PREPARE
        ProjectModel model = Mapper.fromTo(project);
        //EXECUTE
        Mockito.when(repository.findById(PROJECT_ID)).thenReturn(Optional.of(model));
        Project actual1 = underTest.getProject(PROJECT_ID).orElseThrow(ProjectNotFoundException::new);
        Mockito.when(repository.save(Mockito.any(ProjectModel.class))).thenReturn(model);
        Project actual2 = underTest.updateProject(project);
        log.info("{},{}",actual1,actual2);
        //VERIFY
        Assertions.assertAll("gpe of assertions",()->{
            Assertions.assertNotNull(actual1);
            Assertions.assertNotNull(actual2);
            Assertions.assertEquals(actual1.toString(), actual2.toString());
            Mockito.verify(repository, Mockito.atLeast(1)).findById(PROJECT_ID);
            Mockito.verify(repository, Mockito.atLeast(1)).save(Mockito.any(ProjectModel.class));
        });
    }

    @Test
    void loadProjectsAssignedToEmployee() {
        //PREPARE
        List<ProjectModel> pModels = List.of(Mapper.fromTo(project), Mapper.fromTo(project));
        //EXECUTE
        Mockito.when(repository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(pModels);
        List<Project> pBeans = underTest.loadProjectsAssignedToEmployee(EMPLOYEE_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(repository).findByEmployeeId(EMPLOYEE_ID),
                () -> Assertions.assertEquals(2, pBeans.size()));
    }

    @Test
    void loadProjectsOfCompanyC() {
        //PREPARE
        List<ProjectModel> pModels = List.of(Mapper.fromTo(project), Mapper.fromTo(project));
        //EXECUTE
        Mockito.when(repository.findByCompanyId(COMPANY_ID)).thenReturn(pModels);
        List<Project> pBeans = underTest.loadProjectsOfCompanyC(COMPANY_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(repository).findByCompanyId(COMPANY_ID),
                () -> Assertions.assertEquals(2, pBeans.size()));
    }

    @Test
    void getAllProjects() {
        //PREPARE
        List<ProjectModel> projectModels = List.of(Mapper.fromTo(project), Mapper.fromTo(project));
        //EXECUTE
        Mockito.when(repository.findAll()).thenReturn(projectModels);
        List<Project> projects = underTest.getAllProjects();
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(repository).findAll(),
                () -> Assertions.assertEquals(2, projects.size()));
    }

    @Test
    void getRemoteCompanyAPI() {
        //PREPARE
        CompanyModel cModel = Mapper.fromTo(company);
        //EXECUTE
        Mockito.when(companyServiceProxy.loadRemoteApiGetCompany(COMPANY_ID)).thenReturn(cModel);
        Company cBean = underTest.getRemoteCompanyAPI(COMPANY_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(companyServiceProxy).loadRemoteApiGetCompany(COMPANY_ID),
                () -> Assertions.assertNotNull(cBean));
    }

    @Test
    void getRemoteEmployeeAPI() {
        //PREPARE
        EmployeeModel eModel = Mapper.fromTo(employee);
        //EXECUTE
        Mockito.when(employeeServiceProxy.loadRemoteApiGetEmployee(EMPLOYEE_ID)).thenReturn(eModel);
        Employee eBean = underTest.getRemoteEmployeeAPI(EMPLOYEE_ID);
        //VERIFY
        Assertions.assertAll("gpe of assertions",
                () -> Mockito.verify(employeeServiceProxy).loadRemoteApiGetEmployee(EMPLOYEE_ID),
                () -> Assertions.assertNotNull(eBean));
    }
}