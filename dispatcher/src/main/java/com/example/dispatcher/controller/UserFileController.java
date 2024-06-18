package com.example.dispatcher.controller;

import com.example.dispatcher.service.serviceImpl.UserFileService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor

public class UserFileController {

    UserFileService userFileService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
