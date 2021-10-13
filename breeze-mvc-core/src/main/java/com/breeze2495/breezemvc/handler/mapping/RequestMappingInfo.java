package com.breeze2495.breezemvc.handler.mapping;

import com.breeze2495.breezemvc.annnotation.RequestMapping;
import com.breeze2495.breezemvc.http.RequestMethod;

import javax.servlet.annotation.HttpMethodConstraint;

/**
 * @author breeze
 * @date 2021/8/25 10:11 下午
 *
 * 主要用于对应配置在控制器方法上的RequestMapping注解，把RequestMapping注解转换成RequestMappingInfo对象
 */
public class RequestMappingInfo {

    private String path;
    private RequestMethod httpMethod;

    /**
     *
     * @param prefix  前缀
     * @param requestMapping 获取到的path 和 httpMethod
     */
    public RequestMappingInfo(String prefix, RequestMapping requestMapping) {
        this.path = prefix + requestMapping.path();
        this.httpMethod = requestMapping.method();
    }

    public String getPath(){
        return path;
    }

    public RequestMethod getHttpMethod(){
        return httpMethod;
    }
}
