package com.breeze2495.breezemvc.handler.adapter;

import com.breeze2495.breezemvc.ModelAndView;
import com.breeze2495.breezemvc.handler.HandlerMethod;
import com.breeze2495.breezemvc.handler.InvocableHandlerMethod;
import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import com.breeze2495.breezemvc.handler.argument.*;
import com.breeze2495.breezemvc.handler.returnvalue.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author breeze
 * @date 2021/8/31 3:03 下午
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter, InitializingBean {

    private List<HandlerMethodArgumentResolver> customArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;

    private ConversionService conversionService;


    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               HandlerMethod handlerMethod) throws Exception {

        InvocableHandlerMethod invocableHandlerMethod = createInvocableHandlerMethod(handlerMethod);
        ModelAndViewContainer container = new ModelAndViewContainer();

        invocableHandlerMethod.invokeAndHandle(request, response, container);

        return getModelAndView(container);
    }

    private InvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new InvocableHandlerMethod(handlerMethod, argumentResolverComposite,
                returnValueHandlerComposite, conversionService);
    }

    //
    private ModelAndView getModelAndView(ModelAndViewContainer container) {
        //本次请求已经处理完成
        if (container.isRequestHandled()) {
            return null;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setModel(container.getModel());
        modelAndView.setView(container.getView());
        modelAndView.setStatus(container.getStatus());

        return modelAndView;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(conversionService,"ConversionService can not null!");
        if(Objects.isNull(argumentResolverComposite)){
            this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
            List<HandlerMethodArgumentResolver> argumentResolvers = getDefaultArgumentResolvers();
            this.argumentResolverComposite.addResolver(argumentResolvers);
        }

        if(Objects.isNull(returnValueHandlerComposite)){
            this.returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
            List<HandlerMethodReturnValueHandler> returnValueHandlers = getDefaultReturnValueHandlers();
            this.returnValueHandlerComposite.addReturnValueHandler(returnValueHandlers);
        }
    }

    public List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers(){
        List<HandlerMethodArgumentResolver>  argumentResolverList = new ArrayList<>();

        argumentResolverList.add(new ServletRequestMethodArgumentResolver());
        argumentResolverList.add(new ServletResponseMethodArgumentResolver());
        argumentResolverList.add(new ModelMethodArgumentResolver());
        argumentResolverList.add(new RequestParamMethodArgumentResolver());
        argumentResolverList.add(new RequestBodyMethodArgumentResolver());

        if (!CollectionUtils.isEmpty(getCustomArgumentResolvers())){
            argumentResolverList.addAll(getCustomArgumentResolvers());
        }
        return argumentResolverList;
    }

    public List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers(){
        List<HandlerMethodReturnValueHandler> returnValueHandlerList = new ArrayList<>();

        returnValueHandlerList.add(new MapMethodReturnValueHandler());
        returnValueHandlerList.add(new ModelMethodReturnValueHandler());
        returnValueHandlerList.add(new ViewNameMethodReturnValueHandler());
        returnValueHandlerList.add(new ViewMethodReturnValueHandler());
        returnValueHandlerList.add(new ResponseBodyMethodReturnValueHandler());

        if(!CollectionUtils.isEmpty(getCustomReturnValueHandlers())){
            returnValueHandlerList.addAll(getCustomReturnValueHandlers());
        }

        return returnValueHandlerList;
    }

    public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
        return customArgumentResolvers;
    }

    public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> customArgumentResolvers) {
        this.customArgumentResolvers = customArgumentResolvers;
    }

    public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
        return customReturnValueHandlers;
    }

    public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> customReturnValueHandlers) {
        this.customReturnValueHandlers = customReturnValueHandlers;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
