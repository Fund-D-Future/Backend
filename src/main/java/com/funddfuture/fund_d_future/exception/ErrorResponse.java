package com.funddfuture.fund_d_future.exception;

public class ErrorResponse {
    private String error;

    public ErrorResponse(int value, String error) {
        this.error = error;
    }

    // Getters and setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
