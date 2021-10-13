package com.breeze2495.breezemvc.view.resolver;

import com.breeze2495.breezemvc.view.InternalResourceView;
import com.breeze2495.breezemvc.view.RedirectView;
import com.breeze2495.breezemvc.view.View;

/**
 * @author breeze
 * @date 2021/9/7 11:24 上午
 */
public abstract class UrlBasedViewResolver extends AbstractCachingViewResolver {

    private static final String REDIRECT_URL_PREFIX = "redirect:";
    private static final String FORWARD_URL_PREFIX = "forward:";

    private String prefix = "";
    private String suffix = "";

    @Override
    protected View createView(String viewName) {

        //以"redirect:"  返回RedirectView视图
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            return new RedirectView(redirectUrl);
        }

        //以"forward:" 开头，返回InternalResourceView视图
        if (viewName.startsWith(FORWARD_URL_PREFIX)) {
            String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
            return new InternalResourceView(forwardUrl);
        }

        //如果都不是，执行模板方法
        return buildView(viewName);
    }

    protected abstract View buildView(String viewName);

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
