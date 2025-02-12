package io.joework.malabaakapi.service;

import io.joework.malabaakapi.config.security.GoogleAuthenticationToken;
import io.joework.malabaakapi.mapper.UserMapper;
import io.joework.malabaakapi.model.AccountProvider;
import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.dto.LoginResponse;
import io.joework.malabaakapi.repository.UserRepository;
import io.joework.malabaakapi.util.AppUtil;
import io.joework.malabaakapi.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.ParseException;
import java.util.Map;

import static io.joework.malabaakapi.model.Role.PLAYER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    /**
     * perform post login operations, the user get authenticated using the spring security filters (BasicAuthFilter, etc)
     * @param authentication the obj
     * @return a response with the tokens and the user Details
     */
    @Override
    public LoginResponse postLogin(Authentication authentication) {
        var accessToken =  tokenService.generateAccessToken(authentication);
        var refreshToken = tokenService.generateRefreshToken(authentication);
        var user  = (User) authentication.getPrincipal();
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userDto(userMapper.toUserDto(user))
                .build();
    }

    @Override
    public LoginResponse generateNewToken(String refreshToken) {
        var newToken = tokenService.reGenerateAccessToken(refreshToken);
        return LoginResponse.builder()
                .accessToken(newToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public LoginResponse googleAuth(String token) throws ParseException {
        var claims = TokenUtil.getClaims(token);

        String email = claims.getStringClaim("email");
        String name = claims.getStringClaim("name");

        var newUser = userRepository.findByEmail(email)
                        .orElseGet(() -> {
                           var user = new User();
                           user.setEmail(email);
                           user.setAccountProvider(AccountProvider.GOOGLE);
                           user.setFirstName(AppUtil.getUserFirstName(name));
                           user.setLastName(AppUtil.getUserLastName(name));
                           user.setRole(PLAYER);
                           user.setIsEnabled(true);
                           return userRepository.save(user);
                        });
        var auth = new GoogleAuthenticationToken(newUser);
        var accessToken = tokenService.generateAccessToken(auth);
        var refreshToken = tokenService.generateRefreshToken(auth);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userDto(userMapper.toUserDto(newUser))
                .build();
    }
}
