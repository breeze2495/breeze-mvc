package com.breeze2495.breezemvc.controller;

import com.breeze2495.breezemvc.annnotation.RequestMapping;
import com.breeze2495.breezemvc.http.RequestMethod;
import org.springframework.stereotype.Controller;

/**
 * @author breeze
 * @date 2021/8/27 3:03 下午
 *
 * 1.IndexController中我们在类上配置 作为path的前缀 /index;
 *
 * 2.测试的三个方法  解析完成之后 test1() test2() 会出现在注册中心里，并被正确解析成了RequestMappingInfo对象
 *   而没有添加@RequestMapping的test()不会出现
 *
 */

@Controller
@RequestMapping(path = "/index")
public class IndexController {

    @RequestMapping(path = "/test1",method = RequestMethod.GET)
    public void test1(String name1){

    }

    @RequestMapping(path = "/test2",method = RequestMethod.POST)
    public void test2(String name2){

    }

    public void test3(String name3){

    }


}
