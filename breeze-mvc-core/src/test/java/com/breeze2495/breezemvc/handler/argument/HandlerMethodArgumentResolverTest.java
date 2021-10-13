package com.breeze2495.breezemvc.handler.argument;

import com.alibaba.fastjson.JSON;
import com.breeze2495.breezemvc.controller.TestParamResolverController;
import com.breeze2495.breezemvc.handler.HandlerMethod;
import com.breeze2495.breezemvc.pojo.User;
import org.junit.Test;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author breeze
 * @date 2021/8/29 9:33 下午
 *
 */
public class HandlerMethodArgumentResolverTest {


    @Test
    public void test1() throws NoSuchMethodException {
        TestParamResolverController controller = new TestParamResolverController();
        //getMethod()方法，第一个参数为方法名， 后面的参数为：入参
        Method method = controller.getClass().getMethod("requestParamTest",
                String.class,Integer.class, Date.class, HttpServletRequest.class);

        HandlerMethod handlerMethod = new HandlerMethod(controller,method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name","breeze2495");
        request.setParameter("age","100");
        request.setParameter("birthday","2021-08-30 11:45:45");

        HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();
        resolverComposite.addResolver(new RequestParamMethodArgumentResolver());
        resolverComposite.addResolver(new ServletRequestMethodArgumentResolver());
        resolverComposite.addResolver(new RequestBodyMethodArgumentResolver());

        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);

        MockHttpServletResponse response = new MockHttpServletResponse();

        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.getParameters().forEach(methodParameter -> {
            try {
                methodParameter.initParameterNameDiscovery(discoverer);
                Object value = resolverComposite.resolveArgument(methodParameter,request,response,null,conversionService);
                System.out.println(methodParameter.getParameterName() + " : " + value + " type " + value.getClass());
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });

    }

    @Test
    public void test2() throws NoSuchMethodException{

        TestParamResolverController testController = new TestParamResolverController();
        Method method = testController.getClass().getMethod("requestBodyTest", User.class);

        HandlerMethod handlerMethod = new HandlerMethod(testController,method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = new User();
        user.setName("breeze");
        user.setAge(100);
        user.setBirthday(new Date());
        request.setContent(JSON.toJSONString(user).getBytes());

        HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();
        resolverComposite.addResolver(new RequestBodyMethodArgumentResolver());

        MockHttpServletResponse response = new MockHttpServletResponse();

        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.getParameters().forEach(methodParameter ->{
            try {
                methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
                Object value = resolverComposite.resolveArgument(methodParameter,request,response,null,null);
                System.out.println(methodParameter.getParameterName() + " : " + value + " type: " + value.getClass());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }























}
