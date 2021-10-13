package com.breeze2495.breezemvc.config;

import com.breeze2495.breezemvc.DispatcherServlet;
import com.breeze2495.breezemvc.handler.adapter.HandlerAdapter;
import com.breeze2495.breezemvc.handler.adapter.RequestMappingHandlerAdapter;
import com.breeze2495.breezemvc.handler.exception.ExceptionHandlerExceptionResolver;
import com.breeze2495.breezemvc.handler.exception.ExceptionHandlerMethodResolver;
import com.breeze2495.breezemvc.handler.exception.HandlerExceptionResolver;
import com.breeze2495.breezemvc.handler.interceptor.HandlerInterceptor;
import com.breeze2495.breezemvc.handler.interceptor.InterceptorRegistry;
import com.breeze2495.breezemvc.handler.mapping.HandlerMapping;
import com.breeze2495.breezemvc.handler.mapping.RequestMappingHandlerMapping;
import com.breeze2495.breezemvc.interceptor.TestHandlerInterceptor;
import com.breeze2495.breezemvc.interceptor.TestHandlerInterceptor2;
import com.breeze2495.breezemvc.view.View;
import com.breeze2495.breezemvc.view.resolver.ContentNegotiatingViewResolver;
import com.breeze2495.breezemvc.view.resolver.InternalResourceViewResolver;
import com.breeze2495.breezemvc.view.resolver.ViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.util.Collections;


/**
 * @author breeze
 * @date 2021/8/27 3:01 下午
 * <p>
 * 创建JavaConfig配置主类 AppConfig
 */

@Configuration
@ComponentScan(basePackages = "com.breeze2495.breezemvc")
public class AppConfig {

    @Bean
    public ConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);
        return conversionService;
    }





    @Bean
    public HandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public HandlerAdapter handlerAdapter(ConversionService conversionService) {
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        return handlerAdapter;
    }



    @Bean
    public ViewResolver viewResolver() {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));
        return negotiatingViewResolver;
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }



    @Bean
    public HandlerExceptionResolver handlerExceptionResolver(ConversionService conversionService){
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setConversionService(conversionService);
        return resolver;
    }











    /*@Bean
    public RequestMappingHandlerMapping handlerMapping(){

        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

        HandlerInterceptor interceptor1 = new TestHandlerInterceptor();
        interceptorRegistry.addInterceptor(interceptor1)
                .addIncludePatterns("/in_path1")
                .addExcludePatterns("/ex_path1");

        HandlerInterceptor interceptor2 = new TestHandlerInterceptor2();
        interceptorRegistry.addInterceptor(interceptor2)
                .addIncludePatterns("/in_path2","/in_path3");


        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setInterceptors(interceptorRegistry.getMappedInterceptorList());
        return requestMappingHandlerMapping;
    }*/

}
