package io.joework.malabaakapi.api;

import io.joework.malabaakapi.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/verify")
@RequiredArgsConstructor
public class UserVerificationController {

    private final PlayerService playerService;

    @GetMapping
    public String verificationPage(@RequestParam(name = "token") String token){
        boolean isPlayerEnabled = playerService.enablePlayer(token);
        return isPlayerEnabled ?  "verification-success" : "verification-failed";
    }
}
