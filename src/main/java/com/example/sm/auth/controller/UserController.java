package com.example.sm.auth.controller;

import com.example.sm.auth.decorator.UserAddRequest;
import com.example.sm.auth.decorator.UserFilter;
import com.example.sm.auth.decorator.UserResponse;
import com.example.sm.auth.enums.UserSortBy;
import com.example.sm.auth.service.UserService;
import com.example.sm.common.decorator.*;
import com.example.sm.common.enums.Role;
import lombok.SneakyThrows;
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

    //1.update
        //pass id, check  if id not null then perform update else add perform
        //check (userentered  id and database id) matched--> setvariable --> save --> copy -->return userResponse
    //2.add
         //pass role
        //copy properties userAddRequest to userModel
        //set role in database
        //save in database
        //copy properties userModel to userResponse
       //return userResponse

   @SneakyThrows
   @RequestMapping(name = "addOrUpdateUser",value = "/addOrUpdate",method = RequestMethod.POST)
   public DataResponse<UserResponse> addOrUpdateUser(@RequestBody UserAddRequest userAddRequest, @RequestParam(required = false) String id, @RequestParam(required = false) Role role){
       DataResponse<UserResponse> dataResponse=new DataResponse<>();
       dataResponse.setData(userService.addOrUpdateUser(userAddRequest,id,role));
       dataResponse.setStatus(Response.getOkResponse());
       return dataResponse;
   }

    //1.check All by softDeleteFalse
    //2.check if database is empty or not --> not empty --> copy properties -->add userResponse -->return userRe
    @SneakyThrows
   @RequestMapping(name = "getAllUser",value = "/getAll",method = RequestMethod.GET)
   public ListResponse<UserResponse> getAllUser() {
       ListResponse<UserResponse> listResponse= new ListResponse<>();
           listResponse.setData(userService.getAllUser());
           listResponse.setStatus(Response.getSuccessResponse());
       return listResponse;
   }

    //1.pass id
    //2.check user entered id and database id and softDeleteFalse --> matched --> setSoftDelete(true)-->save userModel
    //3.not matched then show error:  user id not found
    @SneakyThrows
    @RequestMapping(name = "getUser",value = "/get{id}",method = RequestMethod.GET)
    public DataResponse<UserResponse> getUser(@RequestParam String id)  {
        DataResponse<UserResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(userService.getUser(id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    //1.pass id
    //2.check user entered id and database id and softDeleteFalse --> matched --> setSoftDelete(true)-->save userModel
    //3.not matched then show error:  user id not found
    @SneakyThrows
    @RequestMapping(name = "deleteUser",value = "/delete{id}",method = RequestMethod.GET)
    public DataResponse<Object> deleteUser(@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        userService.deleteUser(id);
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    //pass FilterSortRequest in (UserFilter,UserSortBy)
        //create UserCustomRepository interface and create method findAllUserByFilterAndSortAndPage
        //UserRepository extend UserCustomRepository
        //UserService create getAllUserWithFilterAndSort method and implement in UserServiceImpl

    @SneakyThrows
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

    //1.pass id
    //2.check user entered id and database id and softDeleteFalse --> matched -->set role
    //JWT token generate
    //response token.
    @SneakyThrows
    @RequestMapping(name = "getToken",value = "/generateToken",method = RequestMethod.GET)
    public TokenResponse<UserResponse> getToken(@RequestParam String id){
        TokenResponse<UserResponse> tokenResponse= new TokenResponse<>();
        UserResponse userResponse = userService.getToken(id);
        tokenResponse.setData(userResponse);
        tokenResponse.setStatus(Response.getOkResponse());
        tokenResponse.setToken(userResponse.getToken());
        return tokenResponse;
    }
    //id
    //username n password
    //check password is empty or not
    //if not empty then encrypt (passwordutils)
    //if empty hen though error
    //if password is found then store the encrypted password to the provided ID's user.
    //save to database
    @SneakyThrows
    @RequestMapping(name = " getEncryptPassword",value = "/getEncryptPassword",method = RequestMethod.GET)
    public DataResponse<String> getEncryptPassword(@RequestParam String id){
        DataResponse<String> dataResponse = new DataResponse<>();
        dataResponse.setData(userService.getEncryptPassword(id));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    //check password is valid or not.
    //STEPS
    //1. pass email,password
    //2. check email is vaild or not
    //3. get password from database
    //4. PasswordUtils is PasswordAuthenticated method call
    //5. if true (Provide User Details)
       // else Error -> Password not matched
    @SneakyThrows
    @RequestMapping(name = "checkUserAuthentication",value = "/getPasswords",method = RequestMethod.GET)
    public DataResponse<UserResponse> checkUserAuthentication(@RequestParam String email, @RequestParam String password){
        DataResponse<UserResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(userService.checkUserAuthentication(email,password));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }

    //pass token
    //getUserIdFromToken() call from jwtTokenUtil
        //check generate id and database id is matched then show user details
        //if not matched then error
    @SneakyThrows
    @RequestMapping(name = "getIdFromToken",value = "/getIdFromToken",method = RequestMethod.GET)
    public TokenResponse<String> getIdFromToken(@RequestParam String token ){
       TokenResponse<String > tokenResponse = new TokenResponse<>();
      tokenResponse.setData(userService.getIdFromToken(token));
      tokenResponse.setStatus(Response.getOkResponse());
      tokenResponse.setToken(token);
       return tokenResponse;
   }

    //pass token
    //method call getIdFromToken()
    //getid from getIdFromToken()
    //Database id set getid
    //validateToken() method call from jwtTokenUtil
    //check if true than show userdetails else error show
   @SneakyThrows
    @RequestMapping(name = "getValidityOfToken",value = "/validate/token",method = RequestMethod.GET)
    public TokenResponse<UserResponse> getValidityOfToken(@RequestParam String token){
        TokenResponse<UserResponse> tokenResponse = new TokenResponse<>();
        tokenResponse.setData(userService.getValidityOfToken(token));
        tokenResponse.setStatus(Response.getTokensucessResponse());
        tokenResponse.setToken(token);
        return tokenResponse;
    }

    @SneakyThrows
    @RequestMapping(name = "getUserLogin",value = "/login",method = RequestMethod.GET)
    public DataResponse<Object> getUserLogin(@RequestParam String email, String password){
        DataResponse<Object> dataResponse = new DataResponse<>();
        userService.getLogin(email, password);
        dataResponse.setStatus(Response.getLoginResponse());
        return dataResponse;
    }

    @SneakyThrows
    @RequestMapping(name = "getOtpVerification",value = "/verification/Otp",method = RequestMethod.GET)
    public DataResponse<UserResponse> getOtpVerification(@RequestParam String otp,@RequestParam String id){
        DataResponse<UserResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(userService.getOtp(otp,id));
        dataResponse.setStatus(Response.getOtpResponse());
        return dataResponse;
    }
}
