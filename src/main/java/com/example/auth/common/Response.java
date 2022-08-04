package com.example.auth.common;

import com.example.auth.common.constant.MessageConstant;
import com.example.auth.common.constant.ResponseConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class Response {
    HttpStatus httpStatus ;
    String status;
    String description;

    public Response(HttpStatus ok, String ok1, String okDescription) {
        this.httpStatus =ok;
        this.status=ok1;
        this.description=okDescription;
    }

    public static Response getOkResponse(){
        return new Response(HttpStatus.OK, ResponseConstant.OK, ResponseConstant.OK);
    }

    public static Response getSuccessResponse() {
        return new Response(HttpStatus.OK, ResponseConstant.SUCCESS, ResponseConstant.USER_FOUND);
    }

    public static Response getNotFoundResponse(String userNotFound) {
        return new Response(HttpStatus.BAD_REQUEST,MessageConstant.USER_ID_NOT_FOUND, MessageConstant.USER_ID_NOT_FOUND);
    }


    public static Response getOhkResponse(){
        return new Response(HttpStatus.OK, ResponseConstant.OK, ResponseConstant.OK);
    }
}
