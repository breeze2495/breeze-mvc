package com.breeze2495.breezemvc.utils;

import org.springframework.core.NamedInheritableThreadLocal;

import javax.servlet.http.HttpServletRequest;

/**
 * @author breeze
 * @date 2021/9/7 2:23 下午
 * <p>
 * 工具类RequestContextHolder ： 在当前线程中存放当前请求的HttpServletRequest
 */
public class RequestContextHolder {

    private static final ThreadLocal<HttpServletRequest> inheritableRequestHolder =
            new NamedInheritableThreadLocal<>("Request context");


    /**
     * 重置当前线程的request
     */
    public static void resetRequest() {
        inheritableRequestHolder.remove();
    }

    public static void setRequest(HttpServletRequest request) {
        inheritableRequestHolder.set(request);
    }

    public static HttpServletRequest getRequest() {
        return inheritableRequestHolder.get();
    }

}
