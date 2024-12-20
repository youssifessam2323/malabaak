package io.joework.malabaakapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.joework.malabaakapi.config.security.WebSecurityConfig;
import io.joework.malabaakapi.model.Role;
import io.joework.malabaakapi.model.dto.SignupRequest;
import io.joework.malabaakapi.model.dto.SignupResponse;
import io.joework.malabaakapi.service.ApplicationUserDetailsService;
import io.joework.malabaakapi.service.SignupService;
import io.joework.malabaakapi.util.MessagesUtil;
import io.joework.malabaakapi.validation.password.ComplexPassword;
import org.apache.tomcat.util.file.Matcher;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static io.joework.malabaakapi.fixtures.UserFixture.*;
import static io.joework.malabaakapi.model.Role.PLAYER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(WebSecurityConfig.class)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignupService signupService;

    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;

    @MockBean
    private MessagesUtil messageUtil;


    @Test
    void whenValidUrlAndMethodAndContentType_thenReturns200() throws Exception {
         SignupRequest signupRequest = getValidSignupRequest();

         when(signupService.signUp(any(SignupRequest.class))).thenReturn(SignupResponse.builder()
                 .email(signupRequest.getEmail())
                 .token("token")
                 .role("PLAYER")
                 .build());

        mockMvc.perform(post("/auth/signup")
                .content(objectMapper.writeValueAsString(signupRequest))
                .contentType("application/json")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").exists())
        .andExpect(jsonPath("$.payload.email").value(signupRequest.getEmail()));

        verify(signupService, times(1)).signUp(any(SignupRequest.class));

    }

    @Test
    void whenInRequestBody_thenReturns400() throws Exception {
        SignupRequest signupRequest = getInValidSignupRequest();

        mockMvc.perform(post("/auth/signup")
                .content(objectMapper.writeValueAsString(signupRequest))
                .contentType("application/json")
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.payload").hasJsonPath());

        verify(signupService, never()).signUp(any(SignupRequest.class));
    }

    @Test
    void whenSignupRequestPasswordIsInValid_ThenReturnStatusCode400AndTheMessage() throws Exception{
        SignupRequest signupRequest = getSignupRequestWithInValidPassword();
        String value = objectMapper.writeValueAsString(ComplexPassword.MESSAGE);
        mockMvc.perform(post("/auth/signup")
                .content(objectMapper.writeValueAsString(signupRequest))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.payload.password").value(value.replace("\"", "")));

    }
}