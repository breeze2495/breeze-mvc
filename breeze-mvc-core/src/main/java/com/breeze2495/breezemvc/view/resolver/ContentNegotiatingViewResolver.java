package com.breeze2495.breezemvc.view.resolver;

import com.breeze2495.breezemvc.utils.RequestContextHolder;
import com.breeze2495.breezemvc.view.RedirectView;
import com.breeze2495.breezemvc.view.View;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author breeze
 * @date 2021/9/7 11:42 上午
 */
public class ContentNegotiatingViewResolver implements ViewResolver, InitializingBean {

    private List<ViewResolver> viewResolvers;
    private List<View> defaultViews;

    @Override
    public View resolveViewName(String viewName) throws Exception {
        List<View> candidateViews = getCandidateViews(viewName);
        View bestView = getBestView(candidateViews);
        if(Objects.nonNull(bestView)){
            return bestView;
        }
        return null;
    }

    /**
     * 根据请求找出最优视图
     *
     * @param candidateViews
     * @return
     */
    private View getBestView(List<View> candidateViews) {

        Optional<View> optional = candidateViews.stream()
                .filter(view -> view instanceof RedirectView)
                .findAny();
        if(optional.isPresent()){
            return optional.get();
        }

        HttpServletRequest request = RequestContextHolder.getRequest();
        Enumeration<String> acceptHeaders = request.getHeaders("Accept");
        while (acceptHeaders.hasMoreElements()){
            for (View view : candidateViews){
                if(acceptHeaders.nextElement().contains(view.getContentType())){
                    return view;
                }
            }
        }
        return null;
    }

    private List<View> getCandidateViews(String viewName) throws Exception {
        List<View> candidateViews = new ArrayList<>();
        for(ViewResolver viewResolver : viewResolvers){
            View view = viewResolver.resolveViewName(viewName);
            if(Objects.nonNull(view)){
                candidateViews.add(view);
            }
        }
        if(!CollectionUtils.isEmpty(defaultViews)){
            candidateViews.addAll(defaultViews);
        }
        return candidateViews;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(viewResolvers, "viewResolvers can not null!");
    }

    public List<ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public List<View> getDefaultViews() {
        return defaultViews;
    }

    public void setDefaultViews(List<View> defaultViews) {
        this.defaultViews = defaultViews;
    }
}
