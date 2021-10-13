package com.breeze2495.breezemvc.handler.mapping;

import com.breeze2495.breezemvc.annnotation.RequestMapping;
import com.breeze2495.breezemvc.exception.NoHandlerFoundException;
import com.breeze2495.breezemvc.handler.HandlerExecutionChain;
import com.breeze2495.breezemvc.handler.HandlerMethod;
import com.breeze2495.breezemvc.handler.interceptor.HandlerInterceptor;
import com.breeze2495.breezemvc.handler.interceptor.MappedInterceptor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author breeze
 * @date 2021/8/26 1:38 下午
 *
 * RequestMappingHandlerMapping 用于完成初始化过程 getHandler的具体实现
 *
 * 在初始化过程中：
 * 1.我们需要获取到容器中所有的Bean对象 ，ApplicationObjectSupport 为我们提供了方便访问容器的方法；
 * 2.我们需要在创建完对象后初始化handlerMethod，所以实现了InitializingBean（提供的afterPropertiesSet()方法能后在对象创建完后，
 *   spring容器会调用这个方法),初始化代码的入口就在afterPropertiesSet中
 *
 * 为什么还需要在RequestMappingHandlerMapping 中保存拦截器的集合？ 其与HandlerExecutionChain中的拦截器的集合有什么区别？
 * 1.RequestMappingHandlerMapping 中的拦截器集合包含了容器中全部的拦截器，而HandlerExecutionChain中只包含匹配了path的拦截器
 *
 */
public class RequestMappingHandlerMapping extends ApplicationObjectSupport implements HandlerMapping, InitializingBean {

    private MappingRegistry mappingRegistry = new MappingRegistry();
    private List<MappedInterceptor> interceptors = new ArrayList<>();

    public void setInterceptors(List<MappedInterceptor> interceptors){this.interceptors = interceptors;}

    public MappingRegistry getMappingRegistry() {
        return mappingRegistry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialHandlerMethods();
    }

    /**
     * 首先我们需要从容器中拿出所有的bean，这里我们使用了spring提供的工具类：
     *
     * Map<String,Object> beansOfMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(obtainApplicationContext(),Object.class);
     * 该方法会返回beanName和bean实例对应的Map
     *
     * 紧接着我们通过stream(jdk1.8及以后)流 将entrySet过滤筛选出带有 @Controller注解的类
     */
    public void initialHandlerMethods(){
        Map<String,Object> beansOfMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(obtainApplicationContext(),Object.class);
        beansOfMap.entrySet()
                .stream()
                .filter(entry -> this.isHandler(entry.getValue()))
                .forEach(entry -> this.detectHandlerMethod(entry.getKey(),entry.getValue()));

    }

    /**
     * 验证该对象是否加上了Controller的注解，加上的话就是我们需要的handler
     *
     * AnnotatedElementUtils.hasAnnotation() 用于判断类是否添加了注解
     *
     * 找出所有的Controller之后，就需要解析出Controller中的所有方法，构建我们所需要的HandlerMethod
     *
     * @param handler
     * @return
     */
    private boolean isHandler(Object handler){
        Class<?> beanType = handler.getClass();
        return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class));
    }

    /**
     * 该方法用于解析出handler中加上@RequestMapping注解的方法
     *
     *  使用工具类MethodIntrospector.selectMethods()方法找出Controller类中所有的方法，并进行遍历，判断该方法是都添加了@RequestMapping注解
     *  如果没有则返回空； 如果添加了注解，则构建并返回RequestMappingInfo对象
     *
     *  如果该Controller类上也添加了@RequestMapping注解，则将其所谓前缀
     *
     *  在所有方法解析完成之后，需要把所有配置有@RequestMapping注解的方法 注册到 MappingRegistry中去
     *
     * @param beanName
     * @param handler
     */
    private void detectHandlerMethod(String beanName,Object handler){
        Class<?> beanType = handler.getClass();
        Map<Method, RequestMappingInfo> methodOfMap = MethodIntrospector.selectMethods(beanType,
                (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> getMappingForMethod(method, beanType));

        //注册
        methodOfMap.forEach(((method, requestMappingInfo) -> this.mappingRegistry.register(requestMappingInfo,handler,method)));
    }

    /**
     * 查找method上面是否有RequestMapping，有的话就构建成RequestMappingInfo
     *
     * @param method
     * @param beanType
     * @return
     */
    private RequestMappingInfo getMappingForMethod(Method method, Class<?> beanType) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if(Objects.isNull(requestMapping)){
            return null;
        }
        String prefix = getPathPrefix(beanType);
        return new RequestMappingInfo(prefix,requestMapping);
        
    }

    /**
     * 获取路径前缀
     *
     * 如果该Controller类上也添加了@RequestMapping注解，则将其所谓前缀
     *
     *
     * @param beanType
     * @return
     */
    private String getPathPrefix(Class<?> beanType) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(beanType, RequestMapping.class);
        if(Objects.isNull(requestMapping)){
            return "";
        }
        return requestMapping.path();
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String lookupPath = request.getRequestURI();
        HandlerMethod handler = mappingRegistry.getHandlerMethodByPath(lookupPath);
        if(Objects.isNull(handler)){
            throw new NoHandlerFoundException(request);
        }
        return createHandlerExecutionChain(lookupPath,handler);
    }

    /**
     * 从所有拦截器中过滤出匹配本次请求path的拦截器，然后创建HandlerExecutionChain对象。
     *
     * @param lookupPath
     * @param handler
     * @return
     */
    private HandlerExecutionChain createHandlerExecutionChain(String lookupPath, HandlerMethod handler) {
        List<HandlerInterceptor> interceptors = this.interceptors.stream()
                .filter(mappedInterceptor -> mappedInterceptor.matches(lookupPath))
                .collect(Collectors.toList());

        return new HandlerExecutionChain(handler,interceptors);

    }


}
