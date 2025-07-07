package com.fastcampus.board.exception.reply;

import com.fastcampus.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class ReplyNotFoundException extends ClientErrorException {

    public ReplyNotFoundException() {
        super(HttpStatus.NOT_FOUND, "comment not found");
    }

    public ReplyNotFoundException(Long commentId) {
        super(HttpStatus.NOT_FOUND, "comment not found");
    }

    public ReplyNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
