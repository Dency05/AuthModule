package com.example.sm.auth.service;

import com.example.sm.auth.decorator.UserAddRequest;
import com.example.sm.auth.decorator.UserFilter;
import com.example.sm.auth.decorator.UserResponse;
import com.example.sm.auth.enums.UserSortBy;
import com.example.sm.auth.model.UserModel;
import com.example.sm.auth.repository.UserRepository;
import com.example.sm.common.constant.MessageConstant;
import com.example.sm.common.decorator.FilterSortRequest;
import com.example.sm.common.decorator.NullAwareBeanUtilsBean;
import com.example.sm.common.enums.PasswordEncryptionType;
import com.example.sm.common.enums.Role;
import com.example.sm.common.exception.AlreadyExistException;
import com.example.sm.common.exception.NotFoundException;
import com.example.sm.common.model.EmailModel;
import com.example.sm.common.model.JWTUser;
import com.example.sm.common.utils.JwtTokenUtil;
import com.example.sm.common.utils.PasswordUtils;
import com.example.sm.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements  UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NullAwareBeanUtilsBean nullAwareBeanUtilsBean;

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    Utils utils;

    @Override
    public UserResponse addOrUpdateUser(UserAddRequest userAddRequest, String id, Role role) throws InvocationTargetException, IllegalAccessException {
        if (id != null) {
           UserModel userResponse1=getUserModel(id);
             userResponse1.setAddress(userAddRequest.getAddress());
             userResponse1.setAge(userAddRequest.getAge());
             userResponse1.setUserName(userAddRequest.getUserName());
             userResponse1.setPassword(userAddRequest.getPassword());
             userResponse1.setFirstName(userAddRequest.getFirstName());
             userResponse1.setMiddleName(userAddRequest.getMiddleName());
             userResponse1.setLastName(userAddRequest.getLastName());
             userResponse1.setEmail(userAddRequest.getEmail());
             userRepository.save(userResponse1);
             UserResponse userResponse=new UserResponse();
             nullAwareBeanUtilsBean.copyProperties(userResponse,userResponse1);
             return userResponse;
        }
        else {
            boolean exists = userRepository.existsByEmailAndSoftDeleteFalse(userAddRequest.getEmail());
            if (exists) {
                throw new AlreadyExistException(MessageConstant.EMAIL_NAME_EXISTS);
            }
            UserModel userModel= new UserModel();
            nullAwareBeanUtilsBean.copyProperties(userModel,userAddRequest);
            userModel.setRole(role);
            userModel.setFullName();
            userRepository.save(userModel);
            UserResponse userResponse= new UserResponse();
            nullAwareBeanUtilsBean.copyProperties(userResponse,userModel);
            return userResponse;
        }
    }

    @Override
    public List<UserResponse> getAllUser() throws InvocationTargetException, IllegalAccessException {
        List<UserModel> userModels= userRepository.findAllBySoftDeleteFalse();
        List<UserResponse> userResponses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userModels)){
            for (UserModel userModel : userModels) {
                UserResponse userResponse = new UserResponse();
                nullAwareBeanUtilsBean.copyProperties(userResponse,userModel);
                userResponses.add(userResponse);
            }
        }
         return userResponses;
    }

    @Override
    public UserResponse getUser(String id) throws InvocationTargetException, IllegalAccessException {
        UserModel userModel=getUserModel(id);
        UserResponse userResponse= new UserResponse();
        nullAwareBeanUtilsBean.copyProperties(userResponse,userModel);
        return userResponse;
    }

    @Override
    public void deleteUser(String id) {
        UserModel userModel=getUserModel(id);
         userModel.setSoftDelete(true);
         userRepository.save(userModel);
    }

    @Override
    public Page<UserResponse> getAllUserWithFilterAndSort(UserFilter filter, FilterSortRequest.SortRequest
            <UserSortBy> sort, PageRequest pagination) {
        return userRepository.findAllUserByFilterAndSortAndPage(filter, sort, pagination);
    }

    @Override
    public UserResponse getToken(String id) throws InvocationTargetException, IllegalAccessException {
       UserModel userModel=getUserModel(id);
       UserResponse userResponse=new UserResponse();
       userResponse.setRole(userModel.getRole());
       JWTUser jwtUser =new JWTUser(id, Collections.singletonList(userResponse.getRole().toString()));
       String token = jwtTokenUtil.generateToken(jwtUser);
       nullAwareBeanUtilsBean.copyProperties(userResponse,userModel);
       userResponse.setToken(token);

       return userResponse;
    }
    //

    @Override
    public String getEncryptPassword(String id) throws InvocationTargetException, IllegalAccessException {
       UserModel userModel=getUserModel(id);
       UserResponse userResponse= new UserResponse();
       userResponse.setUserName(userModel.getUserName());
       userResponse.setPassword(userModel.getPassword());
       if(userModel.getPassword()!=null){
          PasswordUtils passwordUtils=new PasswordUtils();
           String password= passwordUtils.encryptPassword(userModel.getPassword());
           System.out.println(password);
           userModel.setPassword(password);
           userResponse.setPassword(password);
           userRepository.save(userModel);
           String passwords= userResponse.getPassword();
           nullAwareBeanUtilsBean.copyProperties(userResponse,userModel);
           return passwords;
       }
       else{
           throw new NotFoundException(MessageConstant.PASSWORD_EMPTY);
       }
    }

    @Override
    public UserResponse checkUserAuthentication(String email, String password) throws NoSuchAlgorithmException, InvocationTargetException, IllegalAccessException {
        UserModel userModel = getUserEmail(email);
        UserResponse userResponse=new UserResponse();
        userResponse.setPassword(userModel.getPassword());
        PasswordUtils passwordUtils= new PasswordUtils();
        String getPassword = userResponse.getPassword();
        System.out.println(getPassword);
        boolean passwords = passwordUtils.isPasswordAuthenticated(password, getPassword,PasswordEncryptionType.BCRYPT);
        System.out.println(passwords);
        if(passwords){
            userRepository.save(userModel);
            nullAwareBeanUtilsBean.copyProperties(userResponse,userModel);
            return userResponse;
        }
        else
        {
            throw new NotFoundException(MessageConstant.PASSWORD_NOT_MATCHED);
        }
    }

    @Override
    public String getIdFromToken(String token) throws InvocationTargetException, IllegalAccessException {
       String getid =jwtTokenUtil.getUserIdFromToken(token);
        UserResponse userResponse= new UserResponse();
        userResponse.setId(getid);
        String Id = userResponse.getId();
        System.out.println(Id);
        UserModel userModel = getUserModel(Id);
        nullAwareBeanUtilsBean.copyProperties(Id,userModel);
        return Id;
    }

    @Override
    public UserResponse getValidityOfToken(String token) throws InvocationTargetException, IllegalAccessException {
        UserResponse userResponse = new UserResponse();
        String validatetoken = getIdFromToken(token);
        UserModel userResponse1 = getUserModel(validatetoken);
        userResponse.setId(validatetoken);
        String tokenid = userResponse.getId();
        JWTUser jwtUser = new JWTUser(tokenid,new ArrayList<>());
        boolean getValidate = jwtTokenUtil.validateToken(token, jwtUser);
        System.out.println(getValidate);
            if (getValidate) {
                nullAwareBeanUtilsBean.copyProperties(userResponse, userResponse1);
                return userResponse;
            } else {
                throw new NotFoundException(MessageConstant.TOKEN_NOT_VAILD);
            }
    }


    //pass email, password
    //check username n password
    //generate random otp and set into emailmodel
    //set message and To into emailmodel
    //call the method sendEmailNow from utils
    //Otp save in userModel
    @Override
    public void getLogin(String email, String password)  {
        UserModel userModel = getUserEmailAndPassword(email, password);
        EmailModel emailModel= new EmailModel();
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        String otp = String.format("%06d", number);
        emailModel.setMessage(otp);
        emailModel.setTo("sarthak.j@techroversolutions.com");
        emailModel.setSubject("Hello");
        String otps = emailModel.getMessage();
        System.out.println(otps);
        utils.sendEmailNow(emailModel);
        userModel.setOtp(otp);
        userRepository.save(userModel);
    }

    //pass id,otp
    //check existsBy(id,otp)
        //if exists then show all details
        //else error
    @Override
    public UserResponse getOtp(String otp, String id) throws InvocationTargetException, IllegalAccessException {
        boolean exists = userRepository.existsByIdAndOtpAndSoftDeleteFalse(id,otp);
        if (exists) {
            UserModel userResponse1 = getUserModel(id);
            UserResponse userResponse=new UserResponse();
            nullAwareBeanUtilsBean.copyProperties(userResponse, userResponse1);
            return userResponse;
        }
      else{
          throw new NotFoundException(MessageConstant.INVAILD_OTP);
        }
    }


    private UserModel getUserModel(String id){
        return userRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.USER_ID_NOT_FOUND));
    }

    private UserModel getUserEmail(String email){
        return userRepository.findByEmailAndSoftDeleteIsFalse(email).orElseThrow(() -> new NotFoundException(MessageConstant.EMAIL_NOT_FOUND));
    }


    private UserModel getUserEmailAndPassword(String email,String password){
        return userRepository.findByEmailAndPasswordAndSoftDeleteIsFalse(email,password).orElseThrow(() -> new NotFoundException(MessageConstant.INVAILD_AUTHENTICATION));
    }


    /*
    private UserResponse copyUserModel(UserAddRequest userAddRequest) throws InvocationTargetException, IllegalAccessException {
        UserModel userModel= new UserModel();
        nullAwareBeanUtilsBean.copyProperties(userModel,userAddRequest);
        userRepository.save(userModel);
        UserResponse userResponse= new UserResponse();
        nullAwareBeanUtilsBean.copyProperties(userResponse,userModel);
        return userResponse;
    }
    */

}