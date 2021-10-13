package com.breeze2495.breezemvc.handler.returnvalue;

import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author breeze
 * @date 2021/8/30 8:32 下午
 *
 * ViewNameMethodReturnValueHandler：如果返回的是String，则把这个返回值当做时视图的名字
 *
 */
public class ViewNameMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        Class<?> parameterType = returnType.getParameterType();
        return CharSequence.class.isAssignableFrom(parameterType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer container,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (returnValue instanceof CharSequence) {
            String viewName = returnValue.toString();
            container.setViewName(viewName);

        } else if (returnValue != null) {
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }
    }

}
