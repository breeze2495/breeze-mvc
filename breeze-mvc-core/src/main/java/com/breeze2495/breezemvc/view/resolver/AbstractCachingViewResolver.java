package com.breeze2495.breezemvc.view.resolver;

import com.breeze2495.breezemvc.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author breeze
 * @date 2021/9/7 11:02 上午
 */
public abstract class AbstractCachingViewResolver implements ViewResolver {

    private static final Object lock = new Object();

    private static final View UNRESOLVED_VIEW = (model, request, response) -> {
    };

    private Map<String, View> cashedViews = new HashMap<>();


    @Override
    public View resolveViewName(String viewName) throws Exception {

        View view = cashedViews.get(viewName);
        if (Objects.nonNull(view)) {
            return (view != UNRESOLVED_VIEW ? view : null);
        }

        synchronized (lock) {

            view = cashedViews.get(viewName);
            if (Objects.nonNull(view)) {
                return (view != UNRESOLVED_VIEW ? view : null);
            }

            view = createView(viewName);
            if (Objects.isNull(view)) {
                view = UNRESOLVED_VIEW;
            }
            cashedViews.put(viewName, view);

        }

        return (view != UNRESOLVED_VIEW ? view : null);

    }

    protected abstract View createView(String viewName);
}
