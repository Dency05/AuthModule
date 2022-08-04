package com.example.auth.common.decorator;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class GeneralHelper {

    int defaultPageValue = 5;
    public PageRequest getPagination(Integer page, Integer size) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            //size = configService.getConfiguration().getDefaultPageValue();
            size = defaultPageValue;
        }
        return PageRequest.of(page, size);
    }
}
