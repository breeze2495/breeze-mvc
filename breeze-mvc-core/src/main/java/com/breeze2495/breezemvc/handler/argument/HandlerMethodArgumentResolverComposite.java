package com.breeze2495.breezemvc.handler.argument;

import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author breeze
 * @date 2021/8/29 8:54 下午
 *
 * HandlerMethodArgumentResolverComposite: 参数解析器的组合类， 这也是策略模式的常用方式
 *
 * 内部定义一个list ，在resolveArgument中循环所有解析器，找到支持的参数解析器就开始解析，找不到抛出异常
 */
public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver{

    private List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {

        for(HandlerMethodArgumentResolver resolver: argumentResolvers){
            if(resolver.supportsParameter(parameter)){
                return resolver.resolveArgument(parameter,request,response,container,conversionService);
            }
        }

        throw new IllegalArgumentException("UnSupported parameter type [" +
                parameter.getParameterType().getName() + "]. supportsParameter should be called first!");
    }









    public void addResolver(HandlerMethodArgumentResolver resolver){
        this.argumentResolvers.add(resolver);
    }

    public void addResolver(HandlerMethodArgumentResolver... resolvers){
        Collections.addAll(this.argumentResolvers,resolvers);
    }

    public void addResolver(Collection<HandlerMethodArgumentResolver> resolvers){
        this.argumentResolvers.addAll(resolvers);
    }

    public void clear(){
        this.argumentResolvers.clear();
    }
}
