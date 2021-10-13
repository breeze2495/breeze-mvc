package com.breeze2495.breezemvc.annnotation;

import com.breeze2495.breezemvc.http.RequestMethod;

import java.lang.annotation.*;

/**
 * @author breeze
 * @date 2021/8/25 9:53 下午
 *
 * RequestMapping 注解  只提供两种属性
 * 1.path：表示url中的路径
 * 2.method 表示http请求的方式 GET , POST
 *
 */

//这三个注解属于java.lang.annotion下的，加上就表示RequestMapping注解成为了公共API的一部分
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String path();

    RequestMethod method() default RequestMethod.GET;
}
