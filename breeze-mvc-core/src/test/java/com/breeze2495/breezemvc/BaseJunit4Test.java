package com.breeze2495.breezemvc;

import com.breeze2495.breezemvc.config.AppConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author breeze
 * @date 2021/8/27 3:15 下午
 *
 * 创建单元测试基类，主要是配置Spring的测试环境，方便后期开发单元测试
 */

//Junit提供的扩展接口，这里我们指定SpringJUnit4ClassRunner作为Junit测试环境
@RunWith(SpringJUnit4ClassRunner.class)
//加载配置文件
@ContextConfiguration(classes = AppConfig.class)
public class BaseJunit4Test {
}
