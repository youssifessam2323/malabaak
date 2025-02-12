package io.joework.malabaakapi.model.dto;

import io.joework.malabaakapi.validation.password.ComplexPassword;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {
    @NotEmpty
    private String email;
    @NotEmpty
    @ComplexPassword
    private String password;
}
