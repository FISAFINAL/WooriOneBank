package com.fisa.woorionebank.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ResponseDTO <T>{
    private final HttpStatus httpStatus;
    private final String message;
    private final T data;
}
