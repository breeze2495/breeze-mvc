package com.breeze2495.breezemvc.handler.interceptor;

import com.breeze2495.breezemvc.ModelAndView;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author breeze
 * @date 2021/8/28 12:05 下午
 *
 * handler拦截器接口
 */


public interface HandlerInterceptor {

    /**
     * handler执行前，如果返回false，那个handler不会执行
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        return true;
    }

    /**
     * handler 执行后，可以获取handler返回的结果ModelAndView
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    default void postHandle(HttpServletRequest request, HttpServletResponse response,
                                Object handler , @Nullable ModelAndView modelAndView) throws Exception{
    }

    /**
     * 该方法是无论什么情况下都会被调用
     *
     * @param request
     * @param response
     * @param handler
     * @param exception
     * @throws Exception
     */
    default void afterCompletion(HttpServletRequest request,HttpServletResponse response,
                                 Object handler,@Nullable Exception exception) throws Exception{

    }


}
