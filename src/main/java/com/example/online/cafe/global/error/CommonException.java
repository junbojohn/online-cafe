package com.example.online.cafe.global.error;

import com.example.online.cafe.global_notUsed.error.ErrorInterface;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private ErrorInterface errorInterface;

    public CommonException(ErrorInterface errorInterface) {
        super(errorInterface.getErrorMessage());
        this.errorInterface = errorInterface;
    }
}
