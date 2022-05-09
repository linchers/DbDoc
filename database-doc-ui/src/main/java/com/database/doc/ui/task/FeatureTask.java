package com.database.doc.ui.task;

import javafx.concurrent.Task;

import java.util.function.Function;


public class FeatureTask extends Task<TaskResult> {

    private String exception;
    private int status;
    private Function<Object, TaskResult> function;

    public FeatureTask(Function<Object, TaskResult> function) {
        this.function = function;
    }

    public int getStatus() {
        return status;
    }

    public String getExceptions() {
        return exception;
    }

    @Override
    protected TaskResult call() throws Exception {
        try {
            return function.apply(null);
        } catch (Exception e) {
            e.printStackTrace();
            this.exception = e.getMessage();
            return new TaskResult(0);
        }
    }
}