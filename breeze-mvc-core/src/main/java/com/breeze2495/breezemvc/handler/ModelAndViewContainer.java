package com.breeze2495.breezemvc.handler;

import com.breeze2495.breezemvc.http.HttpStatus;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Objects;

/**
 * @author breeze
 * @date 2021/8/29 1:48 下午
 * <p>
 * 该类的使用场景： 每个请求进来都会创建一个对象，主要用于保存Handler处理过程中的Model以及返回的View对象；
 * 该类将会用于参数解析器：HandlerMethodArgumentResolver
 * 以及 Handler返回值解析器： HandlerMethodReturnValueHandler
 * <p>
 * view: 定义类型Object ，因为Handler 即可以返回一个String表示视图的名字，也可以直接返回一个视图对象View
 * Model/ExtendedModelMap: 两者都是Spring中定义的类，可以直接看做成Map
 * status: Http状态码
 * requestHandled: 用于标记本次请求是否已经处理完成，后期将在@ResponseBody中使用到
 */
public class ModelAndViewContainer {

    private Object view;
    private Model model;
    private HttpStatus status;
    private boolean requestHandled = false;


    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public void setViewName(String viewName){
        this.view = viewName;
    }

    public String getViewName(){
        return (this.view instanceof String? (String)this.view : null );
    }

    public Model getModel() {
        if(Objects.isNull(this.model)){
            this.model = new ExtendedModelMap();
        }
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public boolean isRequestHandled() {
        return requestHandled;
    }

    public void setRequestHandled(boolean requestHandled) {
        this.requestHandled = requestHandled;
    }
}
