package com.breeze2495.breezemvc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author breeze
 * @date 2021/9/8 9:20 下午
 */
public class DispatcherServletTest extends BaseJunit4Test{

    @Autowired
    private DispatcherServlet dispatcherServlet;

    @Test
    public void service() throws ServletException, IOException {
        // 1.初始化
        dispatcherServlet.init();

        MockHttpServletRequest request = new MockHttpServletRequest();
        // 2.设置请求参数
        request.setParameter("name","breeze2495");
        // 3.设置请求URI
        request.setRequestURI("/test/dispatch");

        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request,response);

        response.getHeaderNames().forEach(headerName ->
                System.out.println(headerName + " : " + response.getHeader(headerName)));
    }

    @Test
    public void test2() throws ServletException, IOException {
        //1.初始化
        dispatcherServlet.init();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name","breeze2495");
        request.setRequestURI("/test/dispatch2");

        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request,response);

        System.out.println("响应到客户端的数据");
        System.out.println(response.getContentAsString());
    }
}
