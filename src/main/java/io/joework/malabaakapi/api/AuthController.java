package io.joework.malabaakapi.api;

import io.joework.malabaakapi.model.dto.ApiResponse;
import io.joework.malabaakapi.model.dto.SignupRequest;
import io.joework.malabaakapi.model.dto.SignupResponse;
import io.joework.malabaakapi.service.SignupService;
import io.joework.malabaakapi.util.MessagesUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private static final String SIGNUP_SUCCESS_MESSAGE = "user.signup.success";
    private final SignupService signupService;
    private final MessagesUtil messagesUtil;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(
            @RequestBody @Valid SignupRequest signupRequest,
            HttpServletRequest request
    ) throws MessagingException {
        String signupSuccessMessage = messagesUtil.getMessage(SIGNUP_SUCCESS_MESSAGE);
        ApiResponse<SignupResponse> userRegisteredSuccessfully =
                new ApiResponse<>(HttpStatus.OK.value(),
                        signupSuccessMessage,
                        signupService.signUp(signupRequest, request));
        return ResponseEntity.status(HttpStatus.OK).body(userRegisteredSuccessfully);
    }
}
