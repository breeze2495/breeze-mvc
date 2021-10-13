package com.breeze2495.breezemvc;

import com.breeze2495.breezemvc.http.HttpStatus;
import org.springframework.ui.Model;

/**
 * @author breeze
 * @date 2021/8/28 12:10 下午
 */
public class ModelAndView {

    private Object view;
    private Model model;
    private HttpStatus status;

    public void setViewName(String viewName){
        this.view = viewName;
    }

    public String getViewName(){
        return (this.view instanceof String? (String) this.view : null);
    }

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public Model getModel() {
        return model;
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
}
