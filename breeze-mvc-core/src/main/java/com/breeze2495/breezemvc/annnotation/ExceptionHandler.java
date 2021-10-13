package com.breeze2495.breezemvc.annnotation;

import java.lang.annotation.*;

/**
 * @author breeze
 * @date 2021/9/9 10:15 上午
 *
 * ExceptionHandler ： 通过该注解标识出支持处理哪些异常
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionHandler {

    Class<? extends Throwable>[] value() default {};
}
