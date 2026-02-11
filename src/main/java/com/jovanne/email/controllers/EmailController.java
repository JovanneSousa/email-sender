package com.jovanne.email.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "email")
public class EmailController {

    @GetMapping("health")
    public ResponseEntity wakeUp() {
        return ResponseEntity.ok().build();
    }
}
