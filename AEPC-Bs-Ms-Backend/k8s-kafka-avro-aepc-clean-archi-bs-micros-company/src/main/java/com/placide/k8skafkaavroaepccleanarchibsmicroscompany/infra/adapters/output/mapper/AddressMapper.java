package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.output.mapper;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.adapters.input.feignclient.model.AddressModel;
import org.springframework.beans.BeanUtils;

public class AddressMapper {
    private AddressMapper(){}
    public static Address toBean(AddressModel model){
        Address bean = new Address();
        BeanUtils.copyProperties(model,bean);
        return bean;
    }
    public static AddressModel toModel(Address bean){
        AddressModel model = new AddressModel();
        BeanUtils.copyProperties(bean, model);
        return model;
    }
}
