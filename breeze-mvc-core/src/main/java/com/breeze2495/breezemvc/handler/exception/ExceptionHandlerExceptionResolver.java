package com.breeze2495.breezemvc.handler.exception;

import com.breeze2495.breezemvc.ModelAndView;
import com.breeze2495.breezemvc.handler.InvocableHandlerMethod;
import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import com.breeze2495.breezemvc.handler.argument.*;
import com.breeze2495.breezemvc.handler.returnvalue.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author breeze
 * @date 2021/9/9 2:34 下午
 *
 * ExceptionHandlerExceptionResolver ：该类为处理全局异常的核心类，分位两个部分
 *
 * 1.初始化
 * ① 由于需要通过反射调用被ExceptionHandler标注的方法来处理异常，以及HandlerAdapter类型需要参数解析器和返回值处理，
 * 因此在 afterPropertiesSet 需要对参数解析器和返回值处理进行初始化；
 * ② 其次还需要调用 initExceptionHandlerAdviceCache() 完成对 exceptionHandlerAdviceCache的初始化，
 * 从而建立起 ControllerAdviceBean 和 ExceptionHandlerMethodResolver 的关系；
 *
 * 2.resolveException 处理异常返回的ModelAndView
 * ① 先通过调用getExceptionHandlerMethod找到处理异常 ControllerAdviceBean / ExceptionHandlerMethod ，构建出InvocableHandlerMethod
 * ② 执行 exceptionHandlerMethod.invokeAndHandle()
 * ③ 通过 ModelAndViewContainer 构建出 ModelAndView
 */
public class ExceptionHandlerExceptionResolver implements HandlerExceptionResolver, ApplicationContextAware, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;
    private Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache =
            new LinkedHashMap<>();

    private ConversionService conversionService;

    private List<HandlerMethodArgumentResolver> customArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        InvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(ex);
        if (exceptionHandlerMethod == null) {
            return null;
        }

        ModelAndViewContainer container = new ModelAndViewContainer();

        try {

            Throwable cause = ex.getCause();
            if (Objects.nonNull(cause)) {
                exceptionHandlerMethod.invokeAndHandle(request, response, container, cause);
            } else {
                exceptionHandlerMethod.invokeAndHandle(request,response,container,ex);
            }

        } catch (Exception exception) {
            logger.error("exceptionHandlerMethod.invokeAndHandle fail",exception);
            return null;
        }

        if(container.isRequestHandled()){
            return null;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setStatus(container.getStatus());
        modelAndView.setModel(container.getModel());
        modelAndView.setView(container.getView());

        return modelAndView;
    }

    private InvocableHandlerMethod getExceptionHandlerMethod(Exception ex) {
        for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
            ControllerAdviceBean adviceBean = entry.getKey();
            ExceptionHandlerMethodResolver resolver = entry.getValue();
            Method method = resolver.resolveMethod(ex);
            if (method != null) {
                return new InvocableHandlerMethod(adviceBean.getBean(),
                        method,
                        this.argumentResolverComposite,
                        this.returnValueHandlerComposite,
                        this.conversionService);
            }
        }
        return null;
    }


    /**
     * 对参数解析器 和 返回值处理 进行初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.conversionService, "conversionService can not null!");

        //初始化 exceptionHandlerAdviceCache
        initExceptionHandlerAdviceCache();

        if (this.argumentResolverComposite == null) {
            List<HandlerMethodArgumentResolver> argumentResolvers = getDefaultArgumentResolvers();
            this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
            this.argumentResolverComposite.addResolver(argumentResolvers);
        }

        if (this.returnValueHandlerComposite == null) {
            List<HandlerMethodReturnValueHandler> returnValueHandlers = getDefaultReturnValueHandlers();
            this.returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
            this.customReturnValueHandlers.addAll(returnValueHandlers);
        }
    }

    /**
     * 初始化默认返回值处理器
     *
     * @return
     */
    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

        returnValueHandlers.add(new ViewMethodReturnValueHandler());
        returnValueHandlers.add(new ViewNameMethodReturnValueHandler());
        returnValueHandlers.add(new ResponseBodyMethodReturnValueHandler());
        returnValueHandlers.add(new ModelMethodReturnValueHandler());
        returnValueHandlers.add(new MapMethodReturnValueHandler());

        if (!CollectionUtils.isEmpty(returnValueHandlers)) {
            returnValueHandlers.addAll(getCustomReturnValueHandlers());
        }

        return returnValueHandlers;
    }

    /**
     * 初始化默认参数解析处理器
     *
     * @return
     */
    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

        argumentResolvers.add(new ServletRequestMethodArgumentResolver());
        argumentResolvers.add(new ServletResponseMethodArgumentResolver());
        argumentResolvers.add(new RequestParamMethodArgumentResolver());
        argumentResolvers.add(new RequestBodyMethodArgumentResolver());
        argumentResolvers.add(new ModelMethodArgumentResolver());

        if (!CollectionUtils.isEmpty(argumentResolvers)) {
            argumentResolvers.addAll(getCustomArgumentResolvers());
        }
        return argumentResolvers;
    }


    /**
     * 完成对 exceptionHandlerAdviceCache 的初始化
     */
    private void initExceptionHandlerAdviceCache() {
        List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(applicationContext);
        for (ControllerAdviceBean adviceBean : adviceBeans) {
            Class<?> beanType = adviceBean.getBeanType();
            if (beanType == null) {
                throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
            }
            ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(beanType);
            if (resolver.hasExceptionMappings()) {
                this.exceptionHandlerAdviceCache.put(adviceBean, resolver);
            }
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> getExceptionHandlerAdviceCache() {
        return exceptionHandlerAdviceCache;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
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
}
