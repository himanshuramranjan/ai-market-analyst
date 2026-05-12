package com.market.analysis.exception;

public class AICommentaryException extends RuntimeException {
    public AICommentaryException(String message) {
        super(message);
    }

    public AICommentaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
