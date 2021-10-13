package com.breeze2495.breezemvc.interceptor;

import com.breeze2495.breezemvc.handler.interceptor.InterceptorRegistry;
import com.breeze2495.breezemvc.handler.interceptor.MappedInterceptor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author breeze
 * @date 2021/8/28 1:06 下午
 */
public class HandlerInterceptorTest {

    private InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

    @Test
    public void test() throws Exception{
        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();

        interceptorRegistry.addInterceptor(interceptor)
                .addExcludePatterns("/ex_path")
                .addIncludePatterns("/in_path");

        List<MappedInterceptor> mappedInterceptorList = interceptorRegistry.getMappedInterceptorList();

        mappedInterceptorList.get(0).preHandle(null, null, null);
        mappedInterceptorList.get(0).postHandle(null, null, null, null);
        mappedInterceptorList.get(0).afterCompletion(null, null, null, null);
    }
}
