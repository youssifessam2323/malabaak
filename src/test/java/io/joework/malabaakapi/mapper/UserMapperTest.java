package io.joework.malabaakapi.mapper;

import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.dto.SignupRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.joework.malabaakapi.fixtures.UserFixture.getValidSignupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Test
    void givenSignupRequest_MapItToUser(){
        UserMapper mapper = UserMapper.INSTANCE;

        SignupRequest signupRequest = getValidSignupRequest();
        User user = mapper.fromSignupRequest(signupRequest);

        assertNotNull(user);
        assertThat(user).extracting(User::getUsername)
                .isEqualTo(signupRequest.getEmail());
        assertThat(user).extracting(User::getFirstName)
                .isEqualTo(signupRequest.getFirstName());

    }
}