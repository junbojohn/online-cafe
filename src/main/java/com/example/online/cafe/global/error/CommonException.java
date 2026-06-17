package com.example.online.cafe.global.error;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private ErrorInterface errorInterface;

    public CommonException(ErrorInterface errorInterface) {
        super(errorInterface.getErrorMessage());
        this.errorInterface = errorInterface;
    }
}
