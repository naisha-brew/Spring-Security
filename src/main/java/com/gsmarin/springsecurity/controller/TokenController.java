package com.gsmarin.springsecurity.controller;

import com.gsmarin.springsecurity.Entity.LoggingRequest;
import com.gsmarin.springsecurity.Entity.LoggingResponse;
import com.gsmarin.springsecurity.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TokenController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoggingResponse> getToken(@RequestBody LoggingRequest loggingRequest) {
        return userService.login(loggingRequest.username(), loggingRequest.password());
    }

}
