package com.example.sm.auth.service;

import com.example.sm.common.decorator.FilterSortRequest;
import com.example.sm.auth.decorator.UserAddRequest;
import com.example.sm.auth.decorator.UserFilter;
import com.example.sm.auth.decorator.UserResponse;
import com.example.sm.common.enums.Role;
import com.example.sm.auth.enums.UserSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public interface UserService {

 UserResponse addOrUpdateUser(UserAddRequest userAddRequest, String id, Role role) throws InvocationTargetException, IllegalAccessException;

 List<UserResponse> getAllUser() throws InvocationTargetException, IllegalAccessException;

 UserResponse getUser(String id) throws InvocationTargetException, IllegalAccessException;

 //TODO PROVIDE void as method type
 void deleteUser(String id);

 Page<UserResponse> getAllUserWithFilterAndSort(UserFilter filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination);

 UserResponse getToken(String id) throws InvocationTargetException, IllegalAccessException;

 String getEncryptPassword(String id) throws InvocationTargetException, IllegalAccessException;

 UserResponse checkUserAuthentication(String email, String password) throws NoSuchAlgorithmException, InvocationTargetException, IllegalAccessException;

 String getIdFromToken(String token) throws InvocationTargetException, IllegalAccessException;

 UserResponse getValidityOfToken(String token) throws InvocationTargetException, IllegalAccessException;

void getLogin(String email, String password) throws InvocationTargetException, IllegalAccessException;

UserResponse getOtp(String otp,String id) throws InvocationTargetException, IllegalAccessException;
}
