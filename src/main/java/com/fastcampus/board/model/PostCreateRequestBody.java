package com.fastcampus.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestBody {

    private String body; //본문 정보만 필요해

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PostCreateRequestBody that)) return false;
        return Objects.equals(getBody(), that.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getBody());
    }
}
