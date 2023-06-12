package com.example.restful_web_service_practice.common.exception;
// Http status code
// 2XX -> OK
// 400 -> Client가 잘못된 요청을 한 경우
// 500 -> 서버측의 오류
// 사용자가 존재하지 않는 경우 리소스가 없으므로 Notfound 로 처리

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
