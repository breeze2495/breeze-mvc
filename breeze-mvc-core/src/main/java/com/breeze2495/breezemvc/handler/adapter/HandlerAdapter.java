package com.breeze2495.breezemvc.handler.adapter;

import com.breeze2495.breezemvc.ModelAndView;
import com.breeze2495.breezemvc.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author breeze
 * @date 2021/8/31 2:16 下午
 *
 * HandlerAdapter:
 *
 * 该接口我们只定义其中的handle方法 ，
 * 在本项目中：RequestMappingHandlerAdapter只是 HandlerAdapter的一种实现，
 * 在SpringMVC中的HandlerAdapter还有一个supports方法， 对应了SpringMVC中的HandlerAdapter的多个实现，这也是一种策略模式
 *
 */
public interface HandlerAdapter {

    /**
     * 将控制器中的方法返回值封装成 ModelAndView进行返回
     *
     * @param request
     * @param response
     * @param handlerMethod
     * @return
     */
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                         HandlerMethod handlerMethod) throws Exception;
}
