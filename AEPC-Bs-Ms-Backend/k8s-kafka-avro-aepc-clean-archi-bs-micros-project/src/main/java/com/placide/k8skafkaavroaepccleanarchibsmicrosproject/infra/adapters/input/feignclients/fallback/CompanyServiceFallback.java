package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.fallback;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.ExceptionMsg;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.models.CompanyModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.proxies.CompanyServiceProxy;
import org.springframework.stereotype.Component;

@Component
public class CompanyServiceFallback implements CompanyServiceProxy {
    @Override
    public CompanyModel loadRemoteApiGetCompany(String companyId) {
        return CompanyModel.builder()
                .companyId(ExceptionMsg.REMOTE_COMPANY_API_EXCEPTION.getMessage())
                .name(ExceptionMsg.REMOTE_COMPANY_API_EXCEPTION.getMessage())
                .agency(ExceptionMsg.REMOTE_COMPANY_API_EXCEPTION.getMessage())
                .connectedDate(ExceptionMsg.REMOTE_COMPANY_API_EXCEPTION.getMessage())
                .build();
    }
}
