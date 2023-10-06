package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.ProjectAvro;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.project.Project;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.ProjectNotFoundException;

import java.util.List;
import java.util.Optional;

public interface OutputProjectService {
    Project consumeKafkaEventProjectCreate(ProjectAvro projectAvro, String topic);
    Project saveProject(Project project);
    Optional<Project> getProject(String projectId) throws ProjectNotFoundException;
    List<Project> loadProjectByInfo(String name, String description, String state, String employeeId, String companyId);
    Project consumeKafkaEventProjectDelete(ProjectAvro projectAvro,String topic);
    String deleteProject(String projectId) throws ProjectNotFoundException;
    Project consumeKafkaEventProjectUpdate(ProjectAvro projectAvro, String topic);
    Project updateProject (Project payload);
    List<Project> loadProjectsAssignedToEmployee(String employeeId);
    List<Project> loadProjectsOfCompanyC(String companyId);

    List<Project> getAllProjects();
}
