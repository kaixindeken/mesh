package com.tanklab.mathless.exception;

import com.tanklab.mathless.utils.graceful.result.ResponseStatusEnum;

public class MyCustomException extends RuntimeException {

    private ResponseStatusEnum responseStatusEnum;

    public MyCustomException(ResponseStatusEnum responseStatusEnum) {
        super(
                "异常状态码为："
                        + responseStatusEnum.status()
                        + "；\n具体异常信息为："
                        + responseStatusEnum.message());
        this.responseStatusEnum = responseStatusEnum;
    }

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }
}
