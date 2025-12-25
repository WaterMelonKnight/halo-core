package com.watermelon.halo.common;

public class GlobalException extends RuntimeException {
    private final int code;

    public GlobalException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
