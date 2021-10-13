package com.breeze2495.breezemvc.handler.returnvalue;

import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author breeze
 * @date 2021/8/30 3:26 下午
 */
public class MapMethodReturnValueHandler implements HandlerMethodReturnValueHandler{

    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return Map.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    //告诉编译器忽略指定警告
    @SuppressWarnings("unchecked")
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer container,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(returnValue instanceof Map){
            container.getModel().addAllAttributes((Map) returnValue);
        }else if(returnValue != null){
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }





    }
}
