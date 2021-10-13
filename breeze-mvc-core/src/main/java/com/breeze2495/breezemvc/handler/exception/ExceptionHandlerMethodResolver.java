package com.breeze2495.breezemvc.handler.exception;

import com.breeze2495.breezemvc.annnotation.ExceptionHandler;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author breeze
 * @date 2021/9/9 11:22 上午
 * <p>
 * ExceptionHandlerMethodResolver：
 * 当找出了所有被 @ControllerAdvice 标注的类之后，我们还需解析出这些类中哪些方法被注解 @ExceptionHandler标注
 */
public class ExceptionHandlerMethodResolver {
    /**
     * 静态常量 EXCEPTION_HANDLER_METHODS ： 判断方法是否有注解 ExceptionHandler
     */
    public static final ReflectionUtils.MethodFilter EXCEPTION_HANDLER_METHODS = method ->
            AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class);

    private final Map<Class<? extends Throwable>, Method> mappedMethods = new ConcurrentReferenceHashMap<>(16);

    /**
     * 构造方法传入Bean类型
     * MethodIntrospector.selectMethods 过滤出所有被Exception标注的类，保存异常类型和方法的关系
     *
     * @param handlerType
     */
    public ExceptionHandlerMethodResolver(Class<?> handlerType) {
        for (Method method : MethodIntrospector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS)) {

            for (Class<? extends Throwable> exceptionType : detectExceptionMapping(method)) {
                this.mappedMethods.put(exceptionType, method);
            }
        }
    }


    /**
     * 解析出方法上 ExceptionHandler配置的所有异常
     *
     * @param method
     * @return
     */
    private List<Class<? extends Throwable>> detectExceptionMapping(Method method) {
        ExceptionHandler exceptionHandler = AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
        Assert.state(exceptionHandler != null, "No ExceptionHandler annotation!");
        return Arrays.asList(exceptionHandler.value());
    }

    public Map<Class<? extends Throwable>, Method> getMappedMethods() {
        return mappedMethods;
    }

    public boolean hasExceptionMappings(){
        return  !this.mappedMethods.isEmpty();
    }

    /**
     * 通过异常类型找出对应的方法
     *
     * @param exception
     * @return
     */
    public Method resolveMethod(Exception exception){
        Method method = resolverMethodByExceptionType(exception.getClass());
        if(method == null){
            Throwable cause = exception.getCause();
            if(cause == null){
                method = resolverMethodByExceptionType(cause.getClass());
            }
        }
        return method;
    }

    private Method resolverMethodByExceptionType(Class<? extends Throwable> exceptionClass) {
        return mappedMethods.get(exceptionClass);
    }
}






































