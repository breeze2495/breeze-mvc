package com.breeze2495.breezemvc.handler.returnvalue;

import com.alibaba.fastjson.JSON;
import com.breeze2495.breezemvc.annnotation.ResponseBody;
import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author breeze
 * @date 2021/8/30 8:51 下午
 */
public class ResponseBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler{
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer container, HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        //标记本次请求已经处理完成
        container.setRequestHandled(true);

        outPutMessage(response, JSON.toJSONString(returnValue));
    }

    private void outPutMessage(HttpServletResponse response, String message) throws IOException {
        // try-with-resources语句是声明一个或多个资源的try语句。
        // 资源是一个对象，程序完成后必须将其关闭。
        // try-with-resources语句可确保在语句末尾关闭每个资源。
        // 任何实现java.lang.AutoCloseable的对象（包括所有实现java.io.Closeable的对象）都可以用作资源。
        try(PrintWriter out = response.getWriter()){
            out.write(message);
        }
    }
}
