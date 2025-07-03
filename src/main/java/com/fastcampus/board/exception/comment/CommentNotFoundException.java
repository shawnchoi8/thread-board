package com.fastcampus.board.exception.comment;

import com.fastcampus.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends ClientErrorException {

    public CommentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "comment not found");
    }

    public CommentNotFoundException(Long commentId) {
        super(HttpStatus.NOT_FOUND, "comment not found");
    }

    public CommentNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
