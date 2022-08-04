package com.example.sm.common.config;

import com.example.sm.common.decorator.NullAwareBeanUtilsBean;
import com.example.sm.common.decorator.Response;
import com.example.sm.common.decorator.GeneralHelper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralBeans {


	@Bean
    ModelMapper getModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public GeneralHelper getGeneralHelper(){
        return new GeneralHelper();
    }

    @Bean
    public Response getResponse() {
        return new Response();
    }

    @Bean
    public NullAwareBeanUtilsBean beanUtilsBean(){
        return new NullAwareBeanUtilsBean();
    }
	

}