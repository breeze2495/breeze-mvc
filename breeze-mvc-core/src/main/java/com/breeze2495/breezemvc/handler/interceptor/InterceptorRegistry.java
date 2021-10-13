package com.breeze2495.breezemvc.handler.interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author breeze
 * @date 2021/8/28 12:56 下午
 */
public class InterceptorRegistry {
    private List<MappedInterceptor> mappedInterceptorList = new ArrayList<>();


    /**
     * 注册一个Interceptor到Registry
     *
     * @param interceptor
     * @return
     */
    public MappedInterceptor addInterceptor(HandlerInterceptor interceptor){
        MappedInterceptor mappedInterceptor = new MappedInterceptor(interceptor);
        mappedInterceptorList.add(mappedInterceptor);
        return mappedInterceptor;
    }

    public List<MappedInterceptor> getMappedInterceptorList() {
        return this.mappedInterceptorList;
    }
}
