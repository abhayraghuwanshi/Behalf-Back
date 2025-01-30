package com.behalf.delta.exception;

public class WorkflowException extends RuntimeException {
    public WorkflowException(String message) {
        super(message);
    }
    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }
}