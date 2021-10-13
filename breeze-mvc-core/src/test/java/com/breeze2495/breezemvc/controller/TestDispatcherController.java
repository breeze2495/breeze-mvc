package com.breeze2495.breezemvc.controller;

import com.breeze2495.breezemvc.annnotation.*;
import com.breeze2495.breezemvc.exception.TestException;
import com.breeze2495.breezemvc.http.RequestMethod;
import com.breeze2495.breezemvc.vo.ApiResponse;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import sun.security.krb5.internal.PAData;

/**
 * @author breeze
 * @date 2021/9/8 9:01 下午
 */

@ControllerAdvice
@Controller
@RequestMapping(path = "/test")
public class TestDispatcherController {

    @RequestMapping(path = "/dispatch", method = RequestMethod.GET)
    public String dispatch(@RequestParam(name = "name") String name, Model model) {
        System.out.println("TestDispatcherController.dispatch: name = " + name);
        model.addAttribute("name", name);

        return "redirect:/breeze2495.github.com";
    }

    @RequestMapping(path = "/dispatch2", method = RequestMethod.GET)
    public String dispatch2(@RequestParam(name = "name") String name) {
        System.out.println("TestDispatcherController.dispatch2: name = " + name);
        //处理请求的过程中抛出异常
        throw new TestException("testException", name);
    }

    @ResponseBody
    @ExceptionHandler({TestException.class})
    public ApiResponse exceptionHandler(TestException ex){
        System.out.println("执行了 exceptionHandler 方法");
        System.out.println("exception message:" + ex.getMessage());
        return new ApiResponse(200,"exception handle complete",ex.getName());
    }
}
