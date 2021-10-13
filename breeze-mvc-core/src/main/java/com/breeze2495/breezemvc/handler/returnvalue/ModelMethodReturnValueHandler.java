package com.breeze2495.breezemvc.handler.returnvalue;

import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author breeze
 * @date 2021/8/30 3:16 下午
 */
public class ModelMethodReturnValueHandler implements HandlerMethodReturnValueHandler{
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return Model.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer container,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(returnValue == null){
            return;
        }else if(returnValue instanceof Model){
            container.getModel().addAllAttributes(((Model) returnValue).asMap());
        }else {
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }
    }
}
