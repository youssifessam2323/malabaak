package io.joework.malabaakapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.joework.malabaakapi.validation.password.ComplexPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class SignupRequest {
    @NotNull
    @NotEmpty(message = "first name cannot be empty.")
    @JsonProperty("firstName")
    private String firstName;


    @NotEmpty(message = "last name cannot be empty.")
    @JsonProperty("lastName")
    private String lastName;

    @Email(message = "must be valid email format.")
    @JsonProperty("email")
    private String email;

    @ComplexPassword
    @JsonProperty("password")
    private String password;
}
