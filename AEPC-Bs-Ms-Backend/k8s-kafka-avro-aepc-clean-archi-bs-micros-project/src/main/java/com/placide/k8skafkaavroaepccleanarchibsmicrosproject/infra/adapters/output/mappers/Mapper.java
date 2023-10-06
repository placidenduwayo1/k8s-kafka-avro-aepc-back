package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.mappers;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.ProjectAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.employee.Employee;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.project.Project;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.models.CompanyModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.models.EmployeeModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.models.ProjectDto;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.models.ProjectModel;
import org.springframework.beans.BeanUtils;

public class Mapper {
    private Mapper() {
    }

    public static ProjectModel fromTo(Project bean) {
        ProjectModel model = new ProjectModel();
        BeanUtils.copyProperties(bean, model);
        return model;
    }

    public static Project fromTo(ProjectModel model) {
        Project bean = new Project();
        BeanUtils.copyProperties(model, bean);
        return bean;
    }

    public static Project fromTo(ProjectDto dto) {
        Project bean = new Project();
        BeanUtils.copyProperties(dto, bean);
        return bean;
    }

    public static EmployeeModel fromTo(Employee bean) {
        EmployeeModel model = new EmployeeModel();
        BeanUtils.copyProperties(bean, model);
        return model;
    }

    public static Employee fromTo(EmployeeModel model) {
        Employee bean = new Employee();
        BeanUtils.copyProperties(model, bean);
        return bean;
    }

    public static CompanyModel fromTo(Company bean) {
        CompanyModel model = new CompanyModel();
        BeanUtils.copyProperties(bean, model);
        return model;
    }

    public static Company fromTo(CompanyModel model) {
        Company bean = new Company();
        BeanUtils.copyProperties(model, bean);
        return bean;
    }

    public static ProjectAvro fromBeanToAvro(Project project) {
        com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.Employee employeeAvro =
                com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.Employee.newBuilder()
                        .setEmployeeId(project.getEmployeeId())
                        .setFirstname(project.getEmployee().getFirstname())
                        .setLastname(project.getEmployee().getLastname())
                        .setEmail(project.getEmployee().getEmail())
                        .setHireDate(project.getEmployee().getHireDate())
                        .setState(project.getEmployee().getState())
                        .setType(project.getEmployee().getType())
                        .build();
        com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.Company companyAvro =
                com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.Company.newBuilder()
                        .setCompanyId(project.getCompanyId())
                        .setName(project.getCompany().getName())
                        .setAgency(project.getCompany().getAgency())
                        .setType(project.getCompany().getType())
                        .setConnectedDate(project.getCompany().getConnectedDate())
                        .build();

        return ProjectAvro.newBuilder()
                .setProjectId(project.getProjectId())
                .setName(project.getName())
                .setDescription(project.getDescription())
                .setPriority(project.getPriority())
                .setState(project.getState())
                .setCreatedDate(project.getCreatedDate())
                .setEmployeeId(project.getEmployeeId())
                .setEmployee(employeeAvro)
                .setCompanyId(project.getCompanyId())
                .setCompany(companyAvro)
                .build();
    }

    public static Project fromAvroToBean(ProjectAvro projectAvro) {
        Employee employee = new Employee(
                projectAvro.getEmployeeId(),
                projectAvro.getEmployee().getFirstname(),
                projectAvro.getEmployee().getLastname(),
                projectAvro.getEmployee().getEmail(),
                projectAvro.getEmployee().getHireDate(),
                projectAvro.getEmployee().getState(),
                projectAvro.getEmployee().getType());

        Company company = new Company(
                projectAvro.getCompanyId(),
                projectAvro.getCompany().getName(),
                projectAvro.getCompany().getAgency(),
                projectAvro.getCompany().getType(),
                projectAvro.getCompany().getConnectedDate());

        return new Project(
                projectAvro.getProjectId(),
                projectAvro.getName(),
                projectAvro.getDescription(),
                projectAvro.getPriority(),
                projectAvro.getState(),
                projectAvro.getCreatedDate(),
                projectAvro.getEmployeeId(),
                employee,
                projectAvro.getCompanyId(),
                company);
    }
}
