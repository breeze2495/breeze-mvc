package com.breeze2495.breezemvc.annnotation;

import java.lang.annotation.*;

/**
 * @author breeze
 * @date 2021/8/30 8:54 下午
 */

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {
}
