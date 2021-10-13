package com.breeze2495.breezemvc.controller;

import org.springframework.ui.Model;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author breeze
 * @date 2021/8/31 1:09 下午
 */
public class TestInvocableHandlerMethodController {

    /**
     * 测试req 以及 res 能正常注入，并通过res能正常输入内容给前端
     *
     * @param request
     * @param response
     */
    public void testRequestAndResponse(HttpServletRequest request, HttpServletResponse response){
        Assert.notNull(request,"request can not null");
        Assert.notNull(response,"response can not null");

        try(PrintWriter writer = response.getWriter()){
            String name = request.getParameter("name");
            writer.println("InvocableHandlerMethodTest, Param: " + name);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注入model，返回视图名以及能够拿到model中的数据
     *
     * @param model
     * @return
     */
    public String testViewName(Model model){
        model.addAttribute("path","breeze2495.github.com");
        model.addAttribute("hello","moto");
        return "/breeze2495.jsp";
    }
}
