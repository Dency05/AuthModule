package com.example.auth.service;

import com.example.auth.common.decorator.FilterSortRequest;
import com.example.auth.common.constant.MessageConstant;
import com.example.auth.common.exception.NotFoundException;
import com.example.auth.decorator.UserAddRequest;
import com.example.auth.decorator.UserFilter;
import com.example.auth.decorator.UserResponse;
import com.example.auth.enumUser.Role;
import com.example.auth.enumUser.UserSortBy;
import com.example.auth.model.UserModel;
import com.example.auth.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements  UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserResponse addOrUpdateUser(UserAddRequest userAddRequest, String id, Role role) {
        if (id != null) {
            UserModel userModel=getUserModel(id);
             userModel.setAddress(userAddRequest.getAddress());
             userModel.setAge(userAddRequest.getAge());
             userModel.setUserName(userAddRequest.getUserName());
             userModel.setPassword(userAddRequest.getPassword());
             userModel.setFirstName(userAddRequest.getFirstName());
             userModel.setMiddleName(userAddRequest.getMiddleName());
             userModel.setLastName(userAddRequest.getLastName());
             userModel.setEmail(userAddRequest.getEmail());
             return userRepository.save(userModel);
        } else {
            UserResponse userResponse= copyUserModel(userAddRequest);
            return userResponse;
        }
    }

    @Override
    public List<UserResponse> getAllUser() {
        return userRepository.findAllBySoftDeleteFalse();
    }

    @Override
    public UserResponse getUser(String id) {
        UserModel userModel=getUserModel(id);
       return userRepository.save(userModel);
    }

    @Override
    public Object deleteUser(String id) {
         UserModel userModel=getUserModel(id);
         userModel.setSoftDelete(true);
         userRepository.save(userModel);
         return null;
    }

    @Override
    public Page<UserResponse> getAllUserWithFilterAndSort(UserFilter filter, FilterSortRequest.SortRequest
            <UserSortBy> sort, PageRequest pagination) {
        return userRepository.findAllUserByFilterAndSortAndPage(filter, sort, pagination);
    }

    private UserModel getUserModel(String id){
        return userRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    }
    private UserResponse copyUserModel(UserAddRequest userAddRequest){
        UserModel userModel= new UserModel();
        BeanUtils.copyProperties(userAddRequest,userModel);
        return userRepository.save(userModel);
    }

}