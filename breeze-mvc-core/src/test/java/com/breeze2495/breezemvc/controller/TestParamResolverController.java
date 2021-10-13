package com.breeze2495.breezemvc.controller;

import com.breeze2495.breezemvc.annnotation.RequestBody;
import com.breeze2495.breezemvc.annnotation.RequestMapping;
import com.breeze2495.breezemvc.annnotation.RequestParam;
import com.breeze2495.breezemvc.http.RequestMethod;
import com.breeze2495.breezemvc.pojo.User;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author breeze
 * @date 2021/8/29 9:25 下午
 */
@Controller
public class TestParamResolverController {

    @RequestMapping(path = "/requestParamTest",method = RequestMethod.POST)
    public void requestParamTest(@RequestParam(name = "name") String name,
                     @RequestParam(name = "age") Integer age,
                     @RequestParam(name = "birthday")Date birthday,
                     HttpServletRequest request){
    }

    @RequestMapping(path = "/requestBodyTest",method = RequestMethod.POST)
    public void requestBodyTest(@RequestBody User user){

    }

}
