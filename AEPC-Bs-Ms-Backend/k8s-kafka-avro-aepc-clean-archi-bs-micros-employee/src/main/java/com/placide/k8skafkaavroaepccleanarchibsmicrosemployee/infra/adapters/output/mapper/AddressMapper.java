package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.output.mapper;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address;
import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.infra.adapters.input.feignclients.models.AddressModel;
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
