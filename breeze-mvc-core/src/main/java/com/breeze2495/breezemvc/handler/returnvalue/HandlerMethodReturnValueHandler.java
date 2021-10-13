package com.breeze2495.breezemvc.handler.returnvalue;

import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author breeze
 * @date 2021/8/30 3:09 下午
 *
 * HandlerMethodReturnValueHandler : 返回值解析器
 *
 * supportReturnType：判断该解析器是否支持返回值的类型
 * handleReturnValue：实际处理逻辑
 *
 */
public interface HandlerMethodReturnValueHandler {

    boolean supportReturnType(MethodParameter returnType);

    void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
                           ModelAndViewContainer container,
                           HttpServletRequest request, HttpServletResponse response)throws Exception;
}
