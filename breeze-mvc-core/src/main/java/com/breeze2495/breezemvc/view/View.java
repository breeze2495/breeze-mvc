package com.breeze2495.breezemvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author breeze
 * @date 2021/8/30 8:37 下午
 * <p>
 * View 接口
 */
public interface View {

    default String getContentType() {
        return null;
    }

    void render(Map<String, Object> model, HttpServletRequest request,
                HttpServletResponse response) throws Exception;
}
