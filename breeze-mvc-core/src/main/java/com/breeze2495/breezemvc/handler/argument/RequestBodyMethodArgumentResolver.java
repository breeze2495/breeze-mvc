package com.breeze2495.breezemvc.handler.argument;

import com.alibaba.fastjson.JSON;
import com.breeze2495.breezemvc.annnotation.RequestBody;
import com.breeze2495.breezemvc.exception.MissingServletRequestParameterException;
import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

/**
 * @author breeze
 * @date 2021/8/29 4:14 下午
 */
public class RequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    /**
     * 将取出来的字符串通过fastjson转换成参数类型的对象
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {

        String httpMessageBody = this.getHttpRequestBody(request);
        if(!StringUtils.isEmpty(httpMessageBody)){
            return JSON.parseObject(httpMessageBody,parameter.getParameterType());
        }

        RequestBody requestBody = parameter.getParameterAnnotation(RequestBody.class);
        if(Objects.isNull(requestBody)){
            return null;
        }
        if(requestBody.required()){
            throw new MissingServletRequestParameterException(parameter.getParameterName(),
                    parameter.getParameterType().getName());
        }

        return null;

    }

    /**
     * 从request对象流中读取出数据转换成字符串
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String getHttpRequestBody(HttpServletRequest request) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        char[] buff = new char[1024];
        int len;
        while((len = reader.read(buff)) != -1){
            stringBuilder.append(buff,0,len);
        }
        return stringBuilder.toString();
    }
}
