package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee.State;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee.Type;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.models.EmployeeDto;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions.ExceptionsMsg;

public class Validator {
    private Validator() {
    }

    public static boolean isValidEmployee(String firstname, String lastname,
                                          String state, String type, String addressId) {
        return !firstname.isBlank()
                && !lastname.isBlank()
                && !state.isBlank()
                && !type.isBlank()
                && !addressId.isBlank();

    }

    public static boolean checkStateValidity(String state) {
        boolean exits = false;
        for (State iterator: State.values()){
            if(state.equals(iterator.getEmployeeState())) {
                exits=true;
                break;
            }
        }
        return exits;
    }

    public static boolean checkTypeValidity(String type) {
        boolean exist = false;
        for (Type iterator : Type.values()) {
            if (type.equals(iterator.getEmployeeType())) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public static boolean remoteAddressApiUnreachable(String addressId) {
        return addressId.strip().equals(ExceptionsMsg.ADDRESS_API_UNREACHABLE);
    }

    public static void formatter(EmployeeDto employeeDto) {
        employeeDto.setFirstname(employeeDto.getFirstname().strip());
        employeeDto.setLastname(employeeDto.getLastname().strip());
        employeeDto.setState(employeeDto.getState().strip());
        employeeDto.setType(employeeDto.getType().strip());
        employeeDto.setAddressId(employeeDto.getAddressId().strip());
    }

    public static String setEmail(String firstname, String lastname) {
        return firstname.strip()
                .replaceAll("\\s", "-")
                .toLowerCase() + "." + lastname.strip()
                .replaceAll("\\s", "-").toLowerCase() + "@natan.fr";
    }
}
