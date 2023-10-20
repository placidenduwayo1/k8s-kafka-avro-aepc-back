package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.project.Priority;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.project.State;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.ExceptionMsg;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.output.models.ProjectDto;

public class Validator {
    private Validator(){}
    public static boolean isValidProject(String name, String desc, String employeeId, String companyId){
        return !name.isBlank()
                && !desc.isBlank()
                && !employeeId.isBlank()
                && !companyId.isBlank();
    }
    public static boolean isValidProject(int priority){
        boolean valid = false;
        for(Priority it : Priority.values()){
            if(priority==it.getProjectPriority()){
                valid=true;
                break;
            }
        }
        return valid;
    }
    public static boolean isValidProject(String state){
        boolean valid = false;
        for(State it : State.values()){
            if(state.equals(it.getProjectState())){
                valid=true;
                break;
            }
        }
        return valid;
    }
    public static boolean remoteEmployeeApiUnreachable(String employeeId){
        return employeeId.strip().equals(ExceptionMsg.REMOTE_EMPLOYEE_API_EXCEPTION.getMessage());
    }
    public static boolean remoteCompanyApiUnreachable(String companyId){
        return companyId.strip().equals(ExceptionMsg.REMOTE_COMPANY_API_EXCEPTION.getMessage());
    }
    public static void format(ProjectDto dto){
        dto.setName(dto.getName().strip().toUpperCase());
        dto.setDescription(dto.getDescription().strip());
        dto.setState(dto.getState().strip());
        dto.setEmployeeId(dto.getEmployeeId().strip());
        dto.setCompanyId(dto.getCompanyId().strip());
    }
}
