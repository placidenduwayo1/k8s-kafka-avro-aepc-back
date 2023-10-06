package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.fallback;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.models.CompanyModel;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.adapters.input.feignclients.proxies.CompanyServiceProxy;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.Msg.REMOTE_COMPANY_API_UNREACHABLE;
import static com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.Msg.REMOTE_EMPLOYEE_API_UNREACHABLE;

@Component
public class CompanyServiceFallback implements CompanyServiceProxy {
    @Override
    public Optional<CompanyModel> loadRemoteApiGetCompany(String companyId) {
        return Optional.of(CompanyModel.builder()
                .companyId(REMOTE_COMPANY_API_UNREACHABLE)
                .name(REMOTE_EMPLOYEE_API_UNREACHABLE)
                .agency(REMOTE_COMPANY_API_UNREACHABLE)
                .connectedDate(REMOTE_COMPANY_API_UNREACHABLE)
                .build());
    }
}
