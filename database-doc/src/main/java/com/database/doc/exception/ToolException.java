package com.database.doc.exception;

public class ToolException extends CommonException {
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;

    public ToolException(String code, String msg) {
        super(code, msg);
    }
}
