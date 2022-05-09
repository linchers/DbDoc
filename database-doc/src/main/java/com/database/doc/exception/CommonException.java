package com.database.doc.exception;

public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;

    public CommonException(String code, String msg) {
        super(code + ":" + msg);
        this.code = code;
        this.msg = msg;
    }
}
