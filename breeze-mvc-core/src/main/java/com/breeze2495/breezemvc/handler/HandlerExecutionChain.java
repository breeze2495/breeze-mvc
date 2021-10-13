package com.breeze2495.breezemvc.handler;

import com.breeze2495.breezemvc.ModelAndView;
import com.breeze2495.breezemvc.handler.interceptor.HandlerInterceptor;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author breeze
 * @date 2021/8/25 9:41 下午
 *
 *
 */
public class HandlerExecutionChain {

    private HandlerMethod handler;
    private List<HandlerInterceptor> interceptors = new ArrayList<>();
    // 每当执行完一个HandlerInterceptor的preHandle方法之后，interceptorIndex会更改为当前HandlerInterceptor的标
    // 举例说明：假如有三个拦截器，第一个拦截器正常执行完成preHandle方法，在执行第二个拦截器的preHandle返回了false，
    //         那么当调用triggerAfterCompletion只会执行第一个拦截器的afterCompletion
    private int interceptorIndex = -1;


    public HandlerExecutionChain(HandlerMethod handler,List<HandlerInterceptor> interceptors){
        this.handler = handler;
        if(!CollectionUtils.isEmpty(interceptors)){
            this.interceptors = interceptors;
        }
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(CollectionUtils.isEmpty(interceptors)){
            return true;
        }
        for (int i = 0; i < interceptors.size(); i++) {
            HandlerInterceptor interceptor = interceptors.get(i);
            //prehandle 返回 false
            if(!interceptor.preHandle(request,response,this.handler)){
                triggerAfterCompletion(request,response,null);
                return false;
            }
            this.interceptorIndex = i;
        }
        return true;
    }

    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv)throws Exception{
        if (CollectionUtils.isEmpty(interceptors)){
            return;
        }
        for (int i = interceptors.size() - 1; i >= 0 ; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.postHandle(request,response,this.handler,mv);
        }
    }

    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
        if(CollectionUtils.isEmpty(interceptors)){
            return;
        }
        for (int i = this.interceptorIndex; i >= 0 ; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.afterCompletion(request,response,this.handler,ex);
        }
    }

    public HandlerMethod getHandler() {
        return handler;
    }

    public List<HandlerInterceptor> getInterceptors() {
        return interceptors;
    }
}
