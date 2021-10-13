package com.breeze2495.breezemvc.handler.returnvalue;

import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import com.breeze2495.breezemvc.view.View;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author breeze
 * @date 2021/8/30 8:46 下午
 *
 * ViewMethodReturnValueHandler：如果返回的是View对象后，直接放到container中
 */
public class ViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return View.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer container,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (returnValue instanceof View){
            View view = (View) returnValue;
            container.setView(view);
        }else if(returnValue != null){
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }
    }
}
