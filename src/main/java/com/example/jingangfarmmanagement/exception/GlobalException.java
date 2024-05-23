package com.example.jingangfarmmanagement.exception;

import com.example.jingangfarmmanagement.model.BaseResponse;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalException {
//    @ExceptionHandler(IndexOutOfBoundsException.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public BaseResponse badRequestException(Exception ex, WebRequest request) {
//        return new BaseResponse(400, "Bad request","");
//    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public BaseResponse todoException(Exception ex, WebRequest request) {
//        return new BaseResponse(500, "Internal Server Error","");
//    }
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public static BaseResponse notFoundException(String ex) {
//       return new BaseResponse(404, "Not found",ex);
//    }
}
