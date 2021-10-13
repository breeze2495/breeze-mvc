package com.breeze2495.breezemvc.view.resolver;

import com.breeze2495.breezemvc.view.InternalResourceView;
import com.breeze2495.breezemvc.view.View;

/**
 * @author breeze
 * @date 2021/9/7 11:36 上午
 */
public class InternalResourceViewResolver extends UrlBasedViewResolver{

    @Override
    protected View buildView(String viewName) {
        String url = getPrefix() + viewName + getSuffix();
        return new InternalResourceView(url);
    }
}
