package com.example.auth.controller;

import com.example.auth.common.constant.MessageConstant;
import com.example.auth.common.decorator.*;
import com.example.auth.common.DataResponse;
import com.example.auth.common.ListResponse;
import com.example.auth.common.Response;
import com.example.auth.common.exception.EmptyException;
import com.example.auth.common.exception.NotFoundException;
import com.example.auth.decorator.UserAddRequest;
import com.example.auth.decorator.UserFilter;
import com.example.auth.decorator.UserResponse;
import com.example.auth.enumUser.Role;
import com.example.auth.enumUser.UserSortBy;
import com.example.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

   @Autowired
   UserService userService;

   @Autowired
    GeneralHelper generalHelper;

    //TODO Please ADD UPDATE PATH - DG
   @RequestMapping(name = "addOrUpdateUser",value = "/addOrUpdate",method = RequestMethod.POST)
   public DataResponse<UserResponse> addOrUpdateUser(@RequestBody UserAddRequest userAddRequest, @RequestParam(required = false) String id, @RequestParam(required = false) Role role){
       DataResponse<UserResponse> dataResponse=new DataResponse<>();
       try{
           dataResponse.setData(userService.addOrUpdateUser(userAddRequest,id,role));
           dataResponse.setStatus(Response.getOkResponse());
       }
       catch(NotFoundException e) {
           dataResponse.setStatus(Response.getNotFoundResponse(MessageConstant.USER_NOT_FOUND));
       }
       return dataResponse;
   }

   @RequestMapping(name = "getAllUser",value = "/getAll",method = RequestMethod.GET)
   public ListResponse<UserResponse> getAllUser(){
       ListResponse<UserResponse> listResponse= new ListResponse<>();
       try {
           listResponse.setData(userService.getAllUser());
           listResponse.setStatus(Response.getSuccessResponse());
       }
       catch (EmptyException e){
           listResponse.setStatus(Response.getNotFoundResponse(MessageConstant.USER_NOT_FOUND));
       }
       return listResponse;
   }

    @RequestMapping(name = "getUser",value = "/get{id}",method = RequestMethod.GET)
    public DataResponse<UserResponse> getUser(@RequestParam String id) {
        DataResponse<UserResponse> dataResponse = new DataResponse<>();
        try {
            dataResponse.setData(userService.getUser(id));
            dataResponse.setStatus(Response.getOkResponse());
        } catch (NotFoundException e) {
            dataResponse.setStatus(Response.getNotFoundResponse(e.getMessage()));
        }
        return dataResponse;
    }

    @RequestMapping(name = "deleteUser",value = "/delete{id}",method = RequestMethod.GET)
    public DataResponse<Object> deleteUser(@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        try {
            dataResponse.setData(userService.deleteUser(id));
            dataResponse.setStatus(Response.getOkResponse());
        } catch (NotFoundException e) {
            dataResponse.setStatus(Response.getNotFoundResponse(e.getMessage()));
        }
        return dataResponse;
    }

    @RequestMapping(name = "getUserByPagination",value = "/getAll/filter",method = RequestMethod.POST)
    public PageResponse<UserResponse> getUserByPagination(@RequestBody FilterSortRequest<UserFilter, UserSortBy> filterSortRequest){
        PageResponse<UserResponse> pageResponse = new PageResponse<>();
        UserFilter filter = filterSortRequest.getFilter();
        Page<UserResponse> userResponses= userService.getAllUserWithFilterAndSort(filter,
                filterSortRequest.getSort(),
                generalHelper.getPagination(filterSortRequest.getPage().getPage(),filterSortRequest.getPage().getLimit()));
        pageResponse.setData(userResponses);
        pageResponse.setStatus(Response.getOhkResponse());
        return pageResponse;
    }


}
