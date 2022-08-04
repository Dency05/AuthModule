package com.example.auth.common.decorator;

import com.example.auth.common.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T>{
    Page<T> data;
    Response status;
}
