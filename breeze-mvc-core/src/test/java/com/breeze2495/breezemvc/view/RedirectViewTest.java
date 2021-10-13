package com.breeze2495.breezemvc.view;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author breeze
 * @date 2021/9/3 9:47 下午
 */
public class RedirectViewTest {

    @Test
    public void test() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath("/path");

        MockHttpServletResponse response = new MockHttpServletResponse();

        Map<String, Object> model = new HashMap<>();
        model.put("name", "breeze");
        model.put("url", "http://breeze2495.gitee.com");

        RedirectView redirectView = new RedirectView("/redirect/login");
        redirectView.render(model, request, response);

        response.getHeaderNames().forEach(headName -> System.out.println(headName + " : " + response.getHeader(headName)));
    }
}
