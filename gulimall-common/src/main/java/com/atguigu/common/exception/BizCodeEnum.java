package com.atguigu.common.exception;

public enum BizCodeEnum {
    UNKNOW_EXEPTION(10000,"Unknown error."),
    VALID_EXCEPTION( 10001,"Parameter validation failed.");

    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
