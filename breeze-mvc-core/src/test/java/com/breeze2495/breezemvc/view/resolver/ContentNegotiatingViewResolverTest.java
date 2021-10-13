package com.breeze2495.breezemvc.view.resolver;

import com.breeze2495.breezemvc.utils.RequestContextHolder;
import com.breeze2495.breezemvc.view.InternalResourceView;
import com.breeze2495.breezemvc.view.RedirectView;
import com.breeze2495.breezemvc.view.View;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Collections;

/**
 * @author breeze
 * @date 2021/9/7 2:37 下午
 */
public class ContentNegotiatingViewResolverTest {

    @Test
    public void resolveViewName() throws Exception{
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept","text/html");
        RequestContextHolder.setRequest(request);

        View redirectView = resolver.resolveViewName("redirect:/breeze2495.github.com");
        Assert.assertTrue(redirectView instanceof RedirectView);

        View forwardView = resolver.resolveViewName("forward:/breeze2495.github.com");
        Assert.assertTrue(forwardView instanceof InternalResourceView);

        View view = resolver.resolveViewName("/breeze2495.github.com");
        Assert.assertTrue(forwardView instanceof InternalResourceView);


    }
}
