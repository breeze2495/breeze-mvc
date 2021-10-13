package com.breeze2495.breezemvc.controller;

import com.breeze2495.breezemvc.annnotation.RequestMapping;
import com.breeze2495.breezemvc.http.RequestMethod;
import org.springframework.stereotype.Service;

/**
 * @author breeze
 * @date 2021/8/27 3:25 下午
 *
 * - 创建TestConroller类，添加一个方法test4，在类上面标注注解@Service，解析完成后test4不会在注册中心里面找到
 */

@Service
public class ServiceController {

    @RequestMapping(path = "/test4",method = RequestMethod.POST)
    public void test4(String name){

    }
}
