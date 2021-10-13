package com.breeze2495.breezemvc.view;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author breeze
 * @date 2021/9/3 9:06 下午
 * <p>
 * RedirectView 重定向视图
 * <p>
 * url : 表示重定向地址，实际也就是控制器中返回的视图名截取redirect：之后的字符串
 */
public class RedirectView extends AbstractView {

    private String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    protected void renderMergedOutPutModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        String targetUrl = createTargetUrl(model, request);
        response.sendRedirect(targetUrl);

    }

    private String createTargetUrl(Map<String, Object> model, HttpServletRequest request) {
        Assert.notNull(this.url, "url can not null");

        StringBuilder queryParams = new StringBuilder();

        //遍历添加
        model.forEach((key, value) -> {
            queryParams.append(key).append("=").append(value).append("&");
        });

        //将最后一个字符 & 删除
        if (queryParams.length() > 0) {
            queryParams.deleteCharAt(queryParams.length() - 1);
        }

        StringBuilder targetUrl = new StringBuilder();

        if (this.url.startsWith("/")) {
            targetUrl.append(getContextPath(request));
        }

        targetUrl.append(url);

        if (queryParams.length() > 0) {
            targetUrl.append("?").append(queryParams.toString());
        }

        return targetUrl.toString();

    }

    private String getContextPath(HttpServletRequest request) {

        String contextPath = request.getContextPath();
        while (contextPath.startsWith("//")) {
            contextPath = contextPath.substring(1);
        }
        return contextPath;
    }

    public String getUrl() {
        return url;
    }
}
