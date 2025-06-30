package com.fastcampus.board.model.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

/**
 * Client Error가 아닌 Server 단의 Error를 Response 할 때,
 * Client 에게 error 정보 모두 보여줄 필요 없음
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ServerErrorResponse(HttpStatus status) {

}
