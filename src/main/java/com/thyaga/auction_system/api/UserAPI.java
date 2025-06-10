package com.thyaga.auction_system.api;

import com.thyaga.auction_system.data.dto.LoginRequestDTO;
import com.thyaga.auction_system.data.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.thyaga.auction_system.service.UserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserAPI {

    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/auth/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.addUser(userDTO), HttpStatus.CREATED);
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(userService.loginUser(loginRequestDTO), HttpStatus.OK);
    }
}
