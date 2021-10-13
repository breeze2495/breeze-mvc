package com.breeze2495.breezemvc.handler.mapping;

import com.breeze2495.breezemvc.handler.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author breeze
 * @date 2021/8/26 11:20 上午
 *
 * MappingRegistry 是RequestMappingInfo，HandlerMethod 的注册中心 ，当解析完一个控制器的Method后就会向该注册中心注册;
 * 当接收到用户请求后，根据请求的url在该注册中心中找到对应的handlerMethod；
 *
 * 成员属性：
 * 1.pathMappingInfo: url 与 RequestMappingInfo 对应的对象
 * 2.pathHandlerMethod: url 与 HandlerMethod 对应的对象
 */
public class MappingRegistry {

    private Map<String,RequestMappingInfo> pathMappingInfo = new ConcurrentHashMap<>();
    private Map<String, HandlerMethod> pathHandlerMethod = new ConcurrentHashMap<>();


    /**
     * 注册url 与 requestMappingInfo/HandlerMethod 的关系
     *
     * @param mappingInfo
     * @param handler
     * @param method
     */
    public void register(RequestMappingInfo mappingInfo, Object handler, Method method) {
        pathMappingInfo.put(mappingInfo.getPath(),mappingInfo);

        HandlerMethod handlerMethod = new HandlerMethod(handler, method);
        pathHandlerMethod.put(mappingInfo.getPath(),handlerMethod);
    }


    public Map<String, HandlerMethod> getPathHandlerMethod() {
        return pathHandlerMethod;
    }


    /**
     * 根据url获取对应的RequestMappingInfo
     *
     * @param path
     * @return
     */
    public RequestMappingInfo getMappingByPath(String path){
        return this.pathMappingInfo.get(path);
    }

    /**
     * 根据url获取对应的HandlerMethod
     *
     * @param path
     * @return
     */
    public HandlerMethod getHandlerMethodByPath(String path){
        return this.pathHandlerMethod.get(path);
    }















}
