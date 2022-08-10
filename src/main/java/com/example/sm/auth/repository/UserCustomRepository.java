package com.example.sm.auth.repository;

import com.example.sm.common.decorator.FilterSortRequest;
import com.example.sm.auth.decorator.UserFilter;
import com.example.sm.auth.decorator.UserResponse;
import com.example.sm.auth.enums.UserSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserCustomRepository {
    Page<UserResponse> findAllUserByFilterAndSortAndPage(UserFilter filter, FilterSortRequest.SortRequest
            <UserSortBy> sort, PageRequest pagination);
}


