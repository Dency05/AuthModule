package com.example.auth.repository;

import com.example.auth.common.decorator.FilterSortRequest;
import com.example.auth.decorator.UserFilter;
import com.example.auth.decorator.UserResponse;
import com.example.auth.enumUser.UserSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserCustomRepository {
    Page<UserResponse> findAllUserByFilterAndSortAndPage(UserFilter filter, FilterSortRequest.SortRequest
            <UserSortBy> sort, PageRequest pagination);
}


