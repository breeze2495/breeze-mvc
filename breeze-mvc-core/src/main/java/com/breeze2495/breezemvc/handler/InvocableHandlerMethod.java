package com.breeze2495.breezemvc.handler;

import com.breeze2495.breezemvc.handler.argument.HandlerMethodArgumentResolverComposite;
import com.breeze2495.breezemvc.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import com.sun.org.apache.bcel.internal.generic.FALOAD;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author breeze
 * @date 2021/8/31 10:45 上午
 * <p>
 * InvocableHandlerMethod: 是对 Handler 的扩展
 * <p>
 * HandlerMethod：在容器启动过程中搜集控制器的方法，用于定义每个控制器方法
 * InvocableHandlerMethod：用于处理用户的请求调用控制器方法包装处理所需的各种参数和执行处理逻辑
 * <p>
 * 成员变量：
 * 1.ParameterNameDiscoverer： 主要用于查找方法名， 这里我们直接初始化了一个默认实现：DefaultParameterNameDiscoverer
 * 由于InvocableHandlerMethod是用于实现调用控制器方法的，因此也包含了两个对象参数的解析器
 * 2.HandlerMethodArgumentResolverComposite
 * 3.HandlerMethodReturnValueHandlerComposite
 * 另外在参数解析器中需要用到数据的转换，因此需要定义
 * 4.ConversionService
 */
public class InvocableHandlerMethod extends HandlerMethod {

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private HandlerMethodArgumentResolverComposite argumentResolverComposite;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;

    private ConversionService conversionService;

    public InvocableHandlerMethod(HandlerMethod handlerMethod,
                                  HandlerMethodArgumentResolverComposite argumentResolverComposite,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite,
                                  ConversionService conversionService) {
        super(handlerMethod);
        this.argumentResolverComposite = argumentResolverComposite;
        this.returnValueHandlerComposite = returnValueHandlerComposite;
        this.conversionService = conversionService;
    }

    public InvocableHandlerMethod(Object bean, Method method,
                                  HandlerMethodArgumentResolverComposite argumentResolverComposite,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite,
                                  ConversionService conversionService) {
        super(bean, method);
        this.argumentResolverComposite = argumentResolverComposite;
        this.returnValueHandlerComposite = returnValueHandlerComposite;
        this.conversionService = conversionService;
    }


    /**
     * 首先获取到方法参数
     * 遍历方法的所有参数，在处理每个参数之前都需要先调用initParameterNameDiscovery ，然后再通过参数解析器进行解析
     *
     * @param request
     * @param response
     * @param container
     * @param providedArgs
     * @return
     * @throws Exception
     */
    private List<Object> getMethodArgumentValues(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 ModelAndViewContainer container,
                                                 Object... providedArgs) throws Exception {
        Assert.notNull(argumentResolverComposite, "HandlerMethodArgumentResolver can not null!");

        List<MethodParameter> parameters = this.getParameters();
        List<Object> args = new ArrayList<>(parameters.size());
        for (MethodParameter parameter : parameters) {
            parameter.initParameterNameDiscovery(parameterNameDiscoverer);
            Object arg = fingProvidedArgument(parameter,providedArgs);
            if(Objects.nonNull(arg)){
                args.add(arg);
            }
            args.add(argumentResolverComposite.resolveArgument(parameter, request, response, container, conversionService));
        }
        return args;
    }

    private Object fingProvidedArgument(MethodParameter parameter, Object... providedArgs) {
        if(!ObjectUtils.isEmpty(providedArgs)){
            for(Object providedArg : providedArgs){
                if(parameter.getParameterType().isInstance(providedArg)){
                    return providedArg;
                }
            }
        }
        return null;
    }

    /**
     * 解析完所有的参数，通过反射调用控制器中的方法
     *
     * @param request
     * @param response
     * @param container
     * @param providedArgs : 手动传入参数不需要通过参数解析器的情况，例如需要传入一个异常参数的情况
     * @throws Exception
     */
    public void invokeAndHandle(HttpServletRequest request,
                                HttpServletResponse response,
                                ModelAndViewContainer container,
                                Object... providedArgs) throws Exception {

        List<Object> args = this.getMethodArgumentValues(request, response, container, providedArgs);
        Object resultValue = doInvoke(args);

        if (Objects.isNull(resultValue)) {
            //可能用户直接使用response输出内容到前端，直接标记requestHandled为 true
            if (response.isCommitted()) {
                container.setRequestHandled(true);
                return;
            } else {
                throw new IllegalStateException("Controller handler return value is null!");
            }
        }

        container.setRequestHandled(false);
        Assert.state(this.returnValueHandlerComposite != null, "No return value handler");

        // -1 表示方法的返回值
        MethodParameter returnType = new MethodParameter(this.getMethod(), -1);
        this.returnValueHandlerComposite.handleReturnValue(resultValue, returnType, container, request, response);
    }

    private Object doInvoke(List<Object> args) throws InvocationTargetException, IllegalAccessException {
        return this.getMethod().invoke(this.getBean(), args.toArray());
    }


}



















