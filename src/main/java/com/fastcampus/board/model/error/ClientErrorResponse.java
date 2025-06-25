package com.fastcampus.board.model.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

/**
 * Client Error가 발생했을 때, 응답으로 내려줄 HTTP response를 record로 만들어
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ClientErrorResponse(HttpStatus status, Object message) {

}
