package com.breeze2495.breezemvc.handler.adapter;

import com.alibaba.fastjson.JSON;
import com.breeze2495.breezemvc.ModelAndView;
import com.breeze2495.breezemvc.controller.TestInvocableHandlerMethodController;
import com.breeze2495.breezemvc.handler.HandlerMethod;
import org.junit.Test;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import java.lang.reflect.Method;

/**
 * @author breeze
 * @date 2021/9/2 9:04 下午
 */
public class RequestMappingHandlerAdapterTest {

    @Test
    public void handle() throws Exception{

        TestInvocableHandlerMethodController controller = new TestInvocableHandlerMethodController();

        Method method = controller.getClass().getMethod("testViewName", Model.class);
        HandlerMethod handlerMethod = new HandlerMethod(controller,method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);

        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        handlerAdapter.afterPropertiesSet();

        ModelAndView modelAndView = handlerAdapter.handle(request,response,handlerMethod);

        System.out.println(JSON.toJSONString(modelAndView));

    }
}
