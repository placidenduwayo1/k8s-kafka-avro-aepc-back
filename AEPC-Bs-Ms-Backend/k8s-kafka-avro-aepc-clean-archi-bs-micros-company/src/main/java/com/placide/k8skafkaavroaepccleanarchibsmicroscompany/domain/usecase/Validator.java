package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.usecase;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.bean.Type;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.models.CompanyDto;

public class Validator {
    private Validator(){}
    public static boolean areValidCompanyFields(String name, String agency, String type){
        return !name.isBlank()
                 && !agency.isBlank()
                 && !type.isBlank();
    }
    public static boolean checkTypeExists(String type){
        boolean exists = false;
        for(Type it: Type.values()){
            if(type.equals(it.getCompanyType())){
                exists = true;
                break;
            }
        }
        return exists;
    }
    public static void format(CompanyDto dto){
        dto.setName(dto.getName().strip().toUpperCase());
        dto.setAgency(dto.getAgency().strip());
        dto.setType(dto.getType().strip());
    }
}
