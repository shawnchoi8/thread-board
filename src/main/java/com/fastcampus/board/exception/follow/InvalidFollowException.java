package com.fastcampus.board.exception.follow;

import com.fastcampus.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class InvalidFollowException extends ClientErrorException {

    public InvalidFollowException() {
        super(HttpStatus.BAD_REQUEST, "Invalid follow request");
    }

    public InvalidFollowException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public InvalidFollowException(HttpStatus status, String message) {
        super(status, message);
    }
}
