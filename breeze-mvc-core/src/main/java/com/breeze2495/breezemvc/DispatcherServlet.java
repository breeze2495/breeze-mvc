package com.breeze2495.breezemvc;

import com.breeze2495.breezemvc.handler.HandlerExecutionChain;
import com.breeze2495.breezemvc.handler.adapter.HandlerAdapter;
import com.breeze2495.breezemvc.handler.exception.HandlerExceptionResolver;
import com.breeze2495.breezemvc.handler.mapping.HandlerMapping;
import com.breeze2495.breezemvc.utils.RequestContextHolder;
import com.breeze2495.breezemvc.view.View;
import com.breeze2495.breezemvc.view.resolver.ViewResolver;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 * @author breeze
 * @date 2021/9/8 11:08 上午
 * <p>
 * DispatcherServlet : 继承自 HttpServlet，通过使用Servlet API 对HTTP 请求进行响应
 * <p>
 * 其工作大致分为两部分：
 * <p>
 * 1.初始化部分：当Servlet在第一次初始化时会调用init方法， 该方法中对诸如 handlerMapping，ViewResolver等进行初始化
 * 2.对HTTP请求进行响应，作为一个Servlet，当请求到达时Web容器会调用其Service方法；通过RequestContextHolder在线程变量中设置
 * request，然后调用doDispatch完成请求
 */
public class DispatcherServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;
    private Collection<HandlerExceptionResolver> handlerExceptionResolvers;

    @Override
    public void init() {
        this.handlerMapping = this.applicationContext.getBean(HandlerMapping.class);
        this.handlerAdapter = this.applicationContext.getBean(HandlerAdapter.class);
        this.viewResolver = this.applicationContext.getBean(ViewResolver.class);
        this.handlerExceptionResolvers = this.applicationContext.getBeansOfType(HandlerExceptionResolver.class).values();
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("DispatcherServlet.service => uri:{}", req.getRequestURI());
        RequestContextHolder.setRequest(req);

        try {
            doDispatch(req, resp);
        } catch (Exception exception) {
            logger.error("handler the request fail", exception);
        } finally {
            RequestContextHolder.resetRequest();
        }

    }


    /**
     * 执行逻辑：
     * 1.首先通过handlerMapping获取到处理本次请求的 HandlerExecutionChain
     * 2.执行拦截器的前置方法
     * 3.通过handlerAdapter 执行 handler 返回的 ModelAndView
     * 4.执行拦截器的后置方法
     * 5.处理返回的结果 processDispatchResult
     * 6.在处理完成请求后调用 executionChain.triggerAfterCompletion(request,response,dispatchExecution)，
     * 完成afterCompletion方法的调用
     *
     * @param request
     * @param response
     */
    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Exception dispatchException = null;
        HandlerExecutionChain executionChain = null;


        try {
            ModelAndView mv = null;
            try {
                executionChain = this.handlerMapping.getHandler(request);

                if (!executionChain.applyPreHandle(request, response)) {
                    return;
                }

                mv = handlerAdapter.handle(request, response, executionChain.getHandler());

                executionChain.applyPostHandle(request, response, mv);

            } catch (Exception exception) {
                dispatchException = exception;
            }

            processDispatchResult(request, response, mv, dispatchException);
        } catch (Exception exception) {

            dispatchException = exception;
            throw exception;

        } finally {

            if (Objects.nonNull(executionChain)) {
                executionChain.triggerAfterCompletion(request, response, dispatchException);
            }
        }

    }

    /**
     * processDispatchResult ：分为两个逻辑
     * 1.如果是正常的返回ModelAndView ，那么就执行render方法；
     * 2.如果在执行过程中抛出了任何异常，那么就会执行processHandlerException，方便做全局异常处理
     *
     * @param request
     * @param response
     * @param mv
     * @param exception
     * @throws Exception
     */
    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                       ModelAndView mv, Exception exception) throws Exception {

        if (Objects.nonNull(exception)) {
            //error ModelAndView
            mv = processHandlerException(request, response, exception);
        }

        if (Objects.nonNull(mv)) {
            render(mv, request, response);
            return;
        }

        logger.info("No view rendering, null ModelAndView returned");
    }

    /**
     * 首先通过ViewResolver解析出视图,然后再调用View的render方法实施渲染逻辑
     *
     * @param mv
     * @param request
     * @param response
     */
    private void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {

        View view;
        String viewName = mv.getViewName();
        if (!StringUtils.isEmpty(viewName)) {
            view = this.viewResolver.resolveViewName(viewName);
        } else {
            view = (View) mv.getView();
        }

        if (mv.getStatus() != null) {
            response.setStatus(mv.getStatus().getValue());
        }

        view.render(mv.getModel().asMap(), request, response);
    }

    /**
     * 返回 一个异常处理后返回的 ModelAndView
     *
     * @param request
     * @param response
     * @param exception
     * @return
     */
    private ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception {

        if(CollectionUtils.isEmpty(this.handlerExceptionResolvers)){
            throw exception;
        }

        for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers){
            ModelAndView exceptionMV = resolver.resolveException(request,response,exception);
            if(exceptionMV != null){
                return exceptionMV;
            }
        }

        //未找到对应的异常处理器，就继续抛出异常
        throw exception;
    }
}




















