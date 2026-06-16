package com.example.online.cafe.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorInterface {
    //400 BAD_REQUEST
    INVALID_REQUEST_BODY_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "Request Body의 parameter type이 일치하지 않습니다"),
    INVALID_MEMBER_ID(HttpStatus.NOT_FOUND, "유효하지 않은 사용자 ID 입니다."),

    //409 CONFLICT
    ALREADY_EXIST(HttpStatus.CONFLICT, "같은 데이터가 이미 존재합니다"),

    //500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러입니다"),

    //403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다"),

    //401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public int getStatusCode() {
        return httpStatus.value();
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
