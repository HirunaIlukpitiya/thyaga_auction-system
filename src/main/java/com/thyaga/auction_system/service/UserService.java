package com.thyaga.auction_system.service;

import com.thyaga.auction_system.configuration.JwtTokenProvider;
import com.thyaga.auction_system.data.dto.LoginRequestDTO;
import com.thyaga.auction_system.data.dto.UserDTO;
import com.thyaga.auction_system.data.entity.User;
import com.thyaga.auction_system.data.repository.UserRepository;
import com.thyaga.auction_system.exception.ThyagaAuctionException;
import com.thyaga.auction_system.exception.ThyagaAuctionStatus;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public String addUser(@Valid UserDTO userDTO) {

        Optional<User> user = userRepository.getUserByUsernameOrEmail(userDTO.getUsername(), userDTO.getEmail());

        if (user.isPresent()) {
            throw new ThyagaAuctionException(ThyagaAuctionStatus.USERNAME_OR_EMAIL_ALREADY_EXISTS);
        }

        if (!userDTO.getConfirmPassword().equals(userDTO.getPassword())) {
            throw new ThyagaAuctionException(ThyagaAuctionStatus.PASSWORDS_DO_NOT_MATCH);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User newUser = new User();
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);
        LOGGER.info("User registered successfully with username: {}", userDTO.getUsername());
        return "User registered successfully with username: " + userDTO.getUsername();
    }

    public String loginUser(@Valid LoginRequestDTO loginRequestDTO) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequestDTO.getUsernameOrEmail(),
                    loginRequestDTO.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);

            LOGGER.info("User logged in successfully with username: {}", loginRequestDTO.getUsernameOrEmail());
            return jwt;

        } catch (BadCredentialsException e) {
            LOGGER.error("Invalid credentials for user: {}", loginRequestDTO.getUsernameOrEmail());
            throw new ThyagaAuctionException(ThyagaAuctionStatus.INVALID_CREDENTIALS);
        }
    }
}
