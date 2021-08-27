package com.tanklab.mathless.exception;

import com.tanklab.mathless.utils.graceful.result.ResponseStatusEnum;

public class GraceException {

    public static void display(ResponseStatusEnum responseStatusEnum) {
        throw new MyCustomException(responseStatusEnum);
    }
}
