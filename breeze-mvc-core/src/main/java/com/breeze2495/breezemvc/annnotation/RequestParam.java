package com.breeze2495.breezemvc.annnotation;

import java.lang.annotation.*;

/**
 * @author breeze
 * @date 2021/8/29 3:10 下午
 *
 * 当handler的参数带有注解@RequestParam ，则需要从request中取出对应的参数，调用ConversionService转换成正确的类型
 *
 * name: 从request取到的参数的名字，必填
 * required：标记该参数是否必填，默认为true；
 * defaultValue：如果request找不到对应的参数，使用默认的参数
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String name();

    boolean required() default true;

    String defaultValue() default "";
}
