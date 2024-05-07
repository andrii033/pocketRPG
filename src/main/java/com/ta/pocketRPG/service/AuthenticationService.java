package com.ta.pocketRPG.service;

import com.ta.pocketRPG.domain.dto.JwtAuthenticationResponse;
import com.ta.pocketRPG.domain.dto.SignInRequest;
import com.ta.pocketRPG.domain.dto.SignUpRequest;
import com.ta.pocketRPG.domain.model.Role;
import com.ta.pocketRPG.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ta.pocketRPG.domain.model.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;

    /**
     * User registration
     *
     * @param request user data
     * @return token
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        if (!repository.existsByUsername(user.getUsername()) && !repository.existsByEmail(user.getEmail())) {
            userService.create(user);
            var jwt = jwtService.generateToken(user);
            return new JwtAuthenticationResponse(jwt);
        } else {
            return new JwtAuthenticationResponse();
        }

    }

    /**
     * User authentication
     *
     * @param request user data
     * @return token
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
