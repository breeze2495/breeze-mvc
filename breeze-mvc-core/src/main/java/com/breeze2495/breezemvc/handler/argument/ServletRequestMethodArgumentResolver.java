package com.breeze2495.breezemvc.handler.argument;

import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author breeze
 * @date 2021/8/29 2:26 下午
 *
 * ServletRequestMethodArgumentResolver 请求方法参数解析器
 * 1.supportsParameter： 判断参数的类型是不是ServlertRequest的子类，如果是则返回true
 * 2.resolveArgument： 返回true的时候执行，该实现类直接返回request对象
 *
 */
public class ServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return ServletRequest.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {
        return request;
    }
}
