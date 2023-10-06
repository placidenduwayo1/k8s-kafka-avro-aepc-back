package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.ports.output;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.avrobeans.EmployeeAvro;

public interface OutputKafkaProducerEmployeeService {
    EmployeeAvro produceKafkaEventEmployeeCreate(EmployeeAvro employeeAvro);
    EmployeeAvro produceKafkaEventEmployeeDelete(EmployeeAvro employeeAvro);
    EmployeeAvro produceKafkaEventEmployeeEdit(EmployeeAvro employeeAvro);
}
