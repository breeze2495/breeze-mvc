package com.breeze2495.breezemvc.annnotation;

import java.lang.annotation.*;

/**
 * @author breeze
 * @date 2021/8/29 4:15 下午
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBody {
    boolean required() default true;
}
