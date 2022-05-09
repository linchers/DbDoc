package com.database.doc.ui.task;

import java.io.Serializable;

public class TaskResult implements Serializable {

    public static final Integer SILENCE = -1;
    public static final Integer SUCCESS = 1;
    public static final Integer ERROR = 0;

    private Integer code;
    private String message;

    public TaskResult(Integer code) {
        this.code = code;
    }

    public TaskResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static TaskResult build(Integer code, String message) {
        TaskResult result = new TaskResult(code, message);
        return result;
    }

    public static TaskResult success(String message) {
        return TaskResult.build(SUCCESS, message);
    }

    public static TaskResult error(String message) {
        return TaskResult.build(ERROR, message);
    }

    public static TaskResult silence(String message) {
        return TaskResult.build(SILENCE, message);
    }

}
