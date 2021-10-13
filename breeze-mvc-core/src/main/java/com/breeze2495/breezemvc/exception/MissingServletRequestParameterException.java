package com.breeze2495.breezemvc.exception;

import javax.servlet.ServletException;

/**
 * @author breeze
 * @date 2021/8/29 3:37 下午
 */
public class MissingServletRequestParameterException extends ServletException {

    private String parameterName;
    private String parameterType;

    public MissingServletRequestParameterException(String parameterName,String parameterType){
        super("required " + parameterType + " parameterName '" + parameterName + "' is not present");
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }
}
