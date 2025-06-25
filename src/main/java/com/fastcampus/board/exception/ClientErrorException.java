package com.fastcampus.board.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientErrorException extends RuntimeException {
    private final HttpStatus status; //어떤 client 에러인지 나타내줌

    public ClientErrorException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
