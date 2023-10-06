package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.avrobeans.ProjectAvro;

public interface OutputKafkaProducerProjectService {
    ProjectAvro produceKafkaEventProjectCreate(ProjectAvro projectAvro);
    ProjectAvro produceKafkaEventProjectDelete(ProjectAvro projectAvro);
    ProjectAvro produceKafkaEventProjectEdit(ProjectAvro projectAvro);
}
