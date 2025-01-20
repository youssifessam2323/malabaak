package io.joework.malabaakapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/me")
    public String sayHello(Principal principal) {
        return "Hello " + principal.getName();

    }


}
