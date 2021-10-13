package com.breeze2495.breezemvc.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @author breeze
 * @date 2021/9/3 9:35 下午
 * <p>
 * InternalResourceView: 支持JSP，HTML的渲染
 * <p>
 * url：表示JSP文件的路径
 */
public class InternalResourceView extends AbstractView {

    private String url;

    public InternalResourceView(String url) {
        this.url = url;
    }

    /**
     * 将model中的数据全部设置道request中，方便在JSP中通过el表达式取值
     *
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void renderMergedOutPutModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {

        exposeModelAsRequestAttributes(model, request);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(this.url);

        requestDispatcher.forward(request, response);
    }

    /**
     * 把model中的数据放入到request
     *
     * @param model
     * @param request
     */
    private void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request) {

        model.forEach((name,value) -> {
            if(Objects.isNull(value)){
                request.removeAttribute(name);
            }else {
                request.setAttribute(name,value);
            }
        });
    }

    public String getUrl() {
        return url;
    }
}
