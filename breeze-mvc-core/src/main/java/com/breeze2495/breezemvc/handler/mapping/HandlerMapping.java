package com.breeze2495.breezemvc.handler.mapping;

import com.breeze2495.breezemvc.handler.HandlerExecutionChain;

import javax.servlet.http.HttpServletRequest;

/**
 * @author breeze
 * @date 2021/8/25 9:40 下午
 *
 * HandlerMapping接口
 * 该接口中只有一个方法 通过request找到需要执行的handler，包装成HandlerExecutionChain返回
 */
public interface HandlerMapping {

    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
