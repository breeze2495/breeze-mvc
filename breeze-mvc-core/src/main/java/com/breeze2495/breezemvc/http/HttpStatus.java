package com.breeze2495.breezemvc.http;

/**
 * @author breeze
 * @date 2021/8/29 1:51 下午
 */
public enum HttpStatus {
    OK(200,"OK");

    private final int value;
    private final String reasonPhrase;

    HttpStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
