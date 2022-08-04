package com.example.auth.service;

import com.example.auth.common.decorator.FilterSortRequest;
import com.example.auth.decorator.UserAddRequest;
import com.example.auth.decorator.UserFilter;
import com.example.auth.decorator.UserResponse;
import com.example.auth.enumUser.Role;
import com.example.auth.enumUser.UserSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;


public interface UserService {

 //TODO Provide UserResponse Instead of UserModel
UserResponse addOrUpdateUser(UserAddRequest userAddRequest, String id, Role role);

 List<UserResponse> getAllUser();

 UserResponse getUser(String id);

 //TODO PROVIDE void as method type
Object deleteUser(String id);

 Page<UserResponse> getAllUserWithFilterAndSort(UserFilter filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination);
}
