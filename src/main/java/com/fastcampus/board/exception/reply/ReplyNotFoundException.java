package com.fastcampus.board.exception.reply;

import com.fastcampus.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class ReplyNotFoundException extends ClientErrorException {

    public ReplyNotFoundException() {
        super(HttpStatus.NOT_FOUND, "reply not found");
    }

    public ReplyNotFoundException(Long replyId) {
        super(HttpStatus.NOT_FOUND, "reply not found");
    }

    public ReplyNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
