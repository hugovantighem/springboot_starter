package com.controller;

import com.business.api.dto.user.UserCreationDto;
import com.business.api.dto.user.UserDto;
import com.business.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(UserController.ENDPOINT)
public class UserController {

    public static final String ENDPOINT = "/api/users";

    @Autowired
    private UserService service;

    @PostMapping()
    public UserDto create(@Valid @RequestBody UserCreationDto dto) {
        return service.save(dto);
    }
}
