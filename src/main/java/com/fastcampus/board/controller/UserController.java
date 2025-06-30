package com.fastcampus.board.controller;

import com.fastcampus.board.model.user.User;
import com.fastcampus.board.model.user.UserSignUpRequestBody;
import com.fastcampus.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> signUp(@RequestBody UserSignUpRequestBody userSignUpRequestBody) {
        User user = userService.signUp(userSignUpRequestBody.username(), userSignUpRequestBody.password());
        return ResponseEntity.ok(user);
    }
}
