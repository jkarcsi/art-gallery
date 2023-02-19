package jkarcsi.service;

import static jkarcsi.utils.constants.UserMessages.ALREADY_EXIST;
import static jkarcsi.utils.constants.UserMessages.INVALID_DATA;

import javax.servlet.http.HttpServletRequest;

import jkarcsi.dto.user.AppUser;
import jkarcsi.repository.UserRepository;
import jkarcsi.security.JwtTokenProvider;
import jkarcsi.utils.exception.CustomAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public String signin(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getAppUserRoles());
        } catch (AuthenticationException e) {
            throw new CustomAuthException(INVALID_DATA, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signup(AppUser appUser) {
        if (!userRepository.existsByUsername(appUser.getUsername())) {
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            userRepository.save(appUser);
            return jwtTokenProvider.createToken(appUser.getUsername(), appUser.getAppUserRoles());
        } else {
            throw new CustomAuthException(ALREADY_EXIST, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }

    public AppUser search(String username) {
        return userRepository.findByUsername(username);
    }

    public AppUser whoami(HttpServletRequest req) {
        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String username) {
        return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getAppUserRoles());
    }

}
