package com.breeze2495.breezemvc.vo;

/**
 * @author breeze
 * @date 2021/9/10 2:59 下午
 */
public class ApiResponse {

    private int code;
    private String message;
    private String data;

    public ApiResponse(int code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
