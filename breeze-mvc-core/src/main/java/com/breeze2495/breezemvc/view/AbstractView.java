package com.breeze2495.breezemvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author breeze
 * @date 2021/9/3 8:52 下午
 */
public abstract class AbstractView implements View{

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.prepareResponse(request,response);
        this.renderMergedOutPutModel(model,request,response);
    }

    /**
     * 渲染之前的一些准备工作， 例如：设置响应头信息
     *
     * @param request
     * @param response
     */
    protected void prepareResponse(HttpServletRequest request,HttpServletResponse response){

    }

    /**
     *  具体的渲染逻辑
     *
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */
    protected abstract void renderMergedOutPutModel(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response) throws Exception;

}
