package com.breeze2495.breezemvc.controller;

import com.breeze2495.breezemvc.annnotation.RequestMapping;
import com.breeze2495.breezemvc.http.RequestMethod;
import org.springframework.stereotype.Controller;

/**
 * @author breeze
 * @date 2021/8/28 3:31 下午
 */

@Controller
public class TestHandlerController {

    @RequestMapping(path = "/ex_path", method = RequestMethod.POST)
    public void exTest() {
    }

    @RequestMapping(path = "/in_path1", method = RequestMethod.POST)
    public void inTest1() {
    }


    @RequestMapping(path = "/in_path2", method = RequestMethod.POST)
    public void inTest2() {
    }

    @RequestMapping(path = "/in_path3", method = RequestMethod.POST)
    public void inTest3() {
    }

}
