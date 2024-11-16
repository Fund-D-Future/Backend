package com.funddfuture.fund_d_future.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String msg) {
        super(msg);
    }
}