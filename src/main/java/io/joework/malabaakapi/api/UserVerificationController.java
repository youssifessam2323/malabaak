package io.joework.malabaakapi.api;

import io.joework.malabaakapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/verify")
@RequiredArgsConstructor
public class UserVerificationController {

    private final UserService userService;

    @GetMapping
    public String verificationPage(@RequestParam(name = "token") String token){
        boolean isUserEnabled = userService.enableUser(token);
        return isUserEnabled ?  "verification-success" : "verification-failed";
    }
}
