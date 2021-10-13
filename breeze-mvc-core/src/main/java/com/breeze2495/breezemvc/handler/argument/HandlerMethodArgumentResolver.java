package com.breeze2495.breezemvc.handler.argument;

import com.breeze2495.breezemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author breeze
 * @date 2021/8/29 2:08 下午
 *
 * 该接口时一个策略接口，作用时把请求中的数据解析为Controller中方法的参数值
 * 有了他才能让Spring MVC处理入参显得那么高级与自动化
 *
 * supportsParameter：判断当前的参数解析器是否支持传入的参数，true表示支持。
 * resolveArgument: 从request对象中解析出parameter所需的值。
 *                  除了MethodParameter与HttpServletRequest之外，还有ConversionService用于把request中取出的值需要转换成MethodParameter参数的类型
 * 该方法定义与spring MVC稍有不同，主要是为了简化数据转换的过程
 *
 */
public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response,
                           ModelAndViewContainer container, ConversionService conversionService) throws Exception;

}
