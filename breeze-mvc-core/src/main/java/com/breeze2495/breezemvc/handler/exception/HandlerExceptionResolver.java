package com.breeze2495.breezemvc.handler.exception;

import com.breeze2495.breezemvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author breeze
 * @date 2021/9/8 11:24 上午
 *
 */
public interface HandlerExceptionResolver {

    ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,Exception ex);
}
