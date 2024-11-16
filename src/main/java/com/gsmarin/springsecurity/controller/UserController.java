package com.gsmarin.springsecurity.controller;

import com.gsmarin.springsecurity.Entity.User;
import com.gsmarin.springsecurity.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping

public class UserController {

    private final UserService userService;

    @PostMapping("/newUser")
    @Transactional
    @CrossOrigin
    public ResponseEntity<Void> create(@RequestBody User user) {
        try{
            userService.save(user);
            return ResponseEntity.ok().build();
        }catch (ResponseStatusException e) {
            System.out.println(e.getMessage());
            //System.out.println(e.getStatusCode());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_Admin') || hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
}
