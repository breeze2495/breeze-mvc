package com.breeze2495.breezemvc.exception;

/**
 * @author breeze
 * @date 2021/9/10 2:53 下午
 */
public class TestException extends RuntimeException {

    private String name;

    public TestException(String message, String name) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
