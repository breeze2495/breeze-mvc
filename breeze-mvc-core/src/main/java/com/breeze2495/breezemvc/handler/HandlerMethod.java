package com.breeze2495.breezemvc.handler;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author breeze
 * @date 2021/8/25 10:20 下午
 *
 * HandlerMethod 是一个特别重要的对象 ，该对象主要对应了 控制器(Controller） 中的每个方法，也就是实际处理业务的handler
 *
 * 暂时定义了四个属性：
 *
 * 1.bean: 表示该方法的实例对象，也就是Controller实例对象
 * 2.beanType: 用于表示Controller 的类型， 例如 UserController，GoodsController 等等
 * 3.method: 表示Controller中的方法
 * 4.parameters: 表示方法中所有的参数定义， 这里引用了Spring提供的MethodParameter 工具类，
 *              里面封装了一些实用的方法，例如后面会用到的获取方法上的注解等等；
 */
public class HandlerMethod {

    private Object bean;
    private Class<?> beanType;
    private Method method;

    private List<MethodParameter> parameters;

    public HandlerMethod(Object bean,Method method){
        this.bean = bean;
        this.beanType = bean.getClass();
        this.method = method;

        this.parameters = new ArrayList<>();
        int parameterCount = method.getParameterCount();
        for (int index = 0; index < parameterCount; index++) {
            parameters.add(new MethodParameter(method,index));
        }
    }

    public HandlerMethod(HandlerMethod handlerMethod){
        Assert.notNull(handlerMethod,"HandlerMethod is required");
        this.bean = handlerMethod.bean;
        this.beanType = handlerMethod.beanType;
        this.method = handlerMethod.method;
        this.parameters = handlerMethod.parameters;
    }


    public Object getBean() {
        return bean;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Method getMethod() {
        return method;
    }

    public List<MethodParameter> getParameters() {
        return parameters;
    }
}
