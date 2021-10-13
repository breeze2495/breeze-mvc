package com.breeze2495.breezemvc.handler.argument;

import com.breeze2495.breezemvc.annnotation.RequestParam;
import com.breeze2495.breezemvc.exception.MissingServletRequestParameterException;
import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author breeze
 * @date 2021/8/29 2:49 下午
 */
public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {

        RequestParam param = parameter.getParameterAnnotation(RequestParam.class);
        if(Objects.isNull(param)){
            return null;
        }

        String value = request.getParameter(param.name());
        if(StringUtils.isEmpty(value)){
            value = param.defaultValue();
        }

        if(!StringUtils.isEmpty(value)){
            return conversionService.convert(value,parameter.getParameterType());
        }

        //在获取到的值为空的情况下，默认值也为空，但是required又为true ，此时报错
        if(param.required()){
            throw new MissingServletRequestParameterException(parameter.getParameterName(),
                    parameter.getParameterType().getName());
        }

        return null;

    }
}
