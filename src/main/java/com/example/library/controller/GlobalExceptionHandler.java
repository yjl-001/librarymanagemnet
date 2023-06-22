package com.example.library.controller;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.library.Exception.IdentifyException;
import com.example.library.utils.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;
/**
 * 全局的错误与异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private HttpServletResponse response;
    
    /**
     * 处理请求参数缺失的异常
     * @return
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseBody
    public ResponseResult missingParamExceptionHandler() {
        response.setStatus(508);
        return new ResponseResult<>(508, "参数缺失", null);
    }

    /**
     * 处理没有访问请求的异常
     * @return
     */
    @ExceptionHandler(value ={AccessDeniedException.class})
    @ResponseBody
    public ResponseResult accessDeniedExceptionHandler() {
        response.setStatus(403);
        return new ResponseResult<>(403, "没有权限", null);
    }

    /**
     * 处理请求参数类型不匹配的异常
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public ResponseResult matchDataTypeExceptionHandler(){
        response.setStatus(501);
        return new ResponseResult<>(501, "参数类型匹配错误", null);
    }

    /**
     * 处理身份验证过程中抛出的异常
     * @param IdentifyException e 自定义的异常
     * @return
     */
    @ExceptionHandler(value = {IdentifyException.class})
    @ResponseBody
    public ResponseResult IdentifyExceptionHandler(IdentifyException e){
        response.setStatus(403);
        return new ResponseResult<>(403, e.getMessage(), null);
    }

}
