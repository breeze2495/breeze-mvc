package com.breeze2495.breezemvc.handler.exception;

import com.breeze2495.breezemvc.annnotation.ControllerAdvice;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author breeze
 * @date 2021/9/9 10:32 上午
 * <p>
 * ControllerAdviceBean : 该类用于表示被 @ControllerAdvice标注的类
 * 例如：IndexController被标注了ControllerAdvice，那么该类就需要构建一个ControllerAdviceBean对象，
 * beanType为IndexController， bean 就是IndexController的实例对象
 */
public class ControllerAdviceBean {

    private String beanName;
    private Class<?> beanType;
    private Object bean;

    public ControllerAdviceBean(String beanName, Object bean) {
        Assert.notNull(bean, "Bean must not be null!");
        this.beanName = beanName;
        this.bean = bean;
        this.beanType = bean.getClass();
    }

    /**
     * 从容器中找出所有标注了 @Controlleradvice的类， 构建一个ControllerAdviceBean集合返回
     *
     * @param context
     * @return
     */
    public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext context) {
        Map<String, Object> beanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, Object.class);
        return beanMap.entrySet().stream()
                .filter(entry -> hasControllerAdvice(entry.getValue()))
                .map(entry -> new ControllerAdviceBean(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 判断类上是否标有 @ControllerAdvice注解
     *
     * @param bean
     * @return
     */
    private static boolean hasControllerAdvice(Object bean) {
        Class<?> beanType = bean.getClass();
        return (AnnotatedElementUtils.hasAnnotation(beanType, ControllerAdvice.class));
    }

    public String getBeanName() {
        return beanName;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Object getBean() {
        return bean;
    }
}


















