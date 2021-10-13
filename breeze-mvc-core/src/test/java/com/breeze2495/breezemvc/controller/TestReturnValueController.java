package com.breeze2495.breezemvc.controller;

import com.breeze2495.breezemvc.annnotation.ResponseBody;
import com.breeze2495.breezemvc.pojo.User;
import com.breeze2495.breezemvc.view.View;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author breeze
 * @date 2021/8/30 9:19 下午
 */
public class TestReturnValueController {

    @ResponseBody
    public User testResponseBody(){
        User user = new User();
        user.setName("breeze");
        user.setAge(20);
        user.setBirthday(new Date());
        return user;
    }

    public String testViewName(){
        return "/jsp/index.jsp";
    }

    public View testView(){
        return new View() {
            @Override
            public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

            }
        };
    }

    public Model testModel(Model model) {
        model.addAttribute("testModel", "Silently9527");
        return model;
    }

    public Map<String, Object> testMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("testMap", "Silently9527");
        return params;
    }
}
