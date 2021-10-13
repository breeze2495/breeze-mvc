package com.breeze2495.breezemvc.handler;

import com.alibaba.fastjson.JSON;
import com.breeze2495.breezemvc.controller.TestInvocableHandlerMethodController;
import com.breeze2495.breezemvc.handler.argument.HandlerMethodArgumentResolverComposite;
import com.breeze2495.breezemvc.handler.argument.ModelMethodArgumentResolver;
import com.breeze2495.breezemvc.handler.argument.ServletRequestMethodArgumentResolver;
import com.breeze2495.breezemvc.handler.argument.ServletResponseMethodArgumentResolver;
import com.breeze2495.breezemvc.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.breeze2495.breezemvc.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import com.breeze2495.breezemvc.handler.returnvalue.ModelMethodReturnValueHandler;
import com.breeze2495.breezemvc.handler.returnvalue.ViewNameMethodReturnValueHandler;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.ui.ModelExtensionsKt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.VolatileImage;
import java.lang.reflect.Method;

/**
 * @author breeze
 * @date 2021/8/31 1:18 下午
 */
public class InvocableHandlerMethodTest {

    @Test
    public void test1() throws Exception {

        TestInvocableHandlerMethodController controller = new TestInvocableHandlerMethodController();

        Method method = controller.getClass().getMethod("testRequestAndResponse",
                HttpServletRequest.class, HttpServletResponse.class);

        HandlerMethod handlerMethod = new HandlerMethod(controller,method);
        HandlerMethodArgumentResolverComposite argumentResolvers = new HandlerMethodArgumentResolverComposite();
        argumentResolvers.addResolver(new ServletRequestMethodArgumentResolver());
        argumentResolvers.addResolver(new ServletResponseMethodArgumentResolver());

        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(handlerMethod,argumentResolvers,null,null);

        ModelAndViewContainer container = new ModelAndViewContainer();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name","breeze2495");
        MockHttpServletResponse response = new MockHttpServletResponse();

        invocableHandlerMethod.invokeAndHandle(request,response,container);

        System.out.println("前端输出内容： " + response.getContentAsString());


    }

    @Test
    public void test2() throws Exception {

        TestInvocableHandlerMethodController controller = new TestInvocableHandlerMethodController();

        Method method = controller.getClass().getMethod("testViewName", Model.class);

        HandlerMethod handlerMethod = new HandlerMethod(controller,method);
        HandlerMethodArgumentResolverComposite argumentResolvers = new HandlerMethodArgumentResolverComposite();
        argumentResolvers.addResolver(new ModelMethodArgumentResolver());

        HandlerMethodReturnValueHandlerComposite returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
        returnValueHandlers.addReturnValueHandler(new ViewNameMethodReturnValueHandler());

        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(handlerMethod,argumentResolvers,
                returnValueHandlers,null);

        ModelAndViewContainer container = new ModelAndViewContainer();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        invocableHandlerMethod.invokeAndHandle(request,response,container);

        System.out.println("container: " + JSON.toJSONString(container.getModel()));
        System.out.println("viewName: " + container.getViewName());





    }
}
