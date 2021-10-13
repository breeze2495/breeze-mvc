package com.breeze2495.breezemvc.annnotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author breeze
 * @date 2021/9/9 10:13 上午
 *
 * ControllerAdvice : 通过该注解标注的类表示支持处理异常
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ControllerAdvice {
}
