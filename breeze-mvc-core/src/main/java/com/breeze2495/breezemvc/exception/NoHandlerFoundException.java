package com.breeze2495.breezemvc.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author breeze
 * @date 2021/8/27 2:28 下午
 *
 * 自定义异常：NoHandlerFoundException 没有找到该handler对象
 */
public class NoHandlerFoundException extends ServletException {
    //请求方法
    private String httpMethod;
    //请求路径
    private String requestURL;

    public NoHandlerFoundException(HttpServletRequest request) {
        this.httpMethod = request.getMethod();
        this.requestURL = request.getRequestURI();

    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestURL() {
        return requestURL;
    }
}
