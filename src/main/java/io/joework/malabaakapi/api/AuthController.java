package io.joework.malabaakapi.api;

import io.joework.malabaakapi.model.dto.*;
import io.joework.malabaakapi.service.AuthService;
import io.joework.malabaakapi.service.SignupService;
import io.joework.malabaakapi.util.MessagesUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("http:localhost:3000")
public class AuthController {
    private static final String SIGNUP_SUCCESS_MESSAGE = "user.signup.success";
    private final SignupService signupService;
    private final AuthService authService;
    private final MessagesUtil messagesUtil;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(
            @RequestBody @Valid SignupRequest signupRequest
    ) throws MessagingException {
        String signupSuccessMessage = messagesUtil.getMessage(SIGNUP_SUCCESS_MESSAGE);
        log.info("message: {}", signupSuccessMessage);
        ApiResponse<SignupResponse> userRegisteredSuccessfully =
                new ApiResponse<>(HttpStatus.OK.value(),
                        signupSuccessMessage,
                        signupService.signUp(signupRequest));
        return ResponseEntity.status(HttpStatus.OK).body(userRegisteredSuccessfully);
    }

    @PostMapping("token")
    public ResponseEntity<?> authenticate(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.postLogin(authentication));
    }

    @PostMapping("token/refresh")
    public ResponseEntity<?> newAccessToken(@RequestHeader("refresh_token") String refreshToken){
        var res = authService.generateNewToken(refreshToken);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/google")
    public ResponseEntity<?> verifyGoogleToken(@RequestBody Map<String, String> request) throws ParseException {
        String token = request.get("token");
        var res = authService.googleAuth(token);
        return ResponseEntity.ok(res);
    }
}
