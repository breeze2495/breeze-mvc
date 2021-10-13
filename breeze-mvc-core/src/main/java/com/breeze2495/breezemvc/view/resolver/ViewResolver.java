package com.breeze2495.breezemvc.view.resolver;

import com.breeze2495.breezemvc.view.View;

/**
 * @author breeze
 * @date 2021/9/7 10:57 上午
 *
 * ViewResolver : 将viewName解析成View对象
 */
public interface ViewResolver {

    View resolveViewName(String viewName) throws Exception;
}
