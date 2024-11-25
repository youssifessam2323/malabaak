package io.joework.malabaakapi.repository;

import io.joework.malabaakapi.config.security.password.PasswordConfig;
import io.joework.malabaakapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static io.joework.malabaakapi.fixtures.UserFixture.getUserEntityNewRecord;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integration")
@DataJpaTest(properties = "spring.main.web-application-type=servlet")
@ImportAutoConfiguration(classes = {SecurityAutoConfiguration.class, PasswordConfig.class})
class UserRepositoryTest {

    @Autowired
    private UserRepository classUnderTest;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final User user = getUserEntityNewRecord();

    @BeforeEach
    void setup(){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Test
    @DisplayName("Test1: testing save user")
    void testSaveAndRetrieveUser(){
        User savedUser = classUnderTest.save(user);

        Optional<User> byEmail = classUnderTest.findByEmail(user.getEmail());
        assertTrue(byEmail.isPresent());
        assertEquals(savedUser.getEmail(), byEmail.get().getEmail());
        assertEquals(savedUser.getFirstName(), byEmail.get().getFirstName());
        assertEquals(savedUser.getLastName(), byEmail.get().getLastName());
        assertEquals(savedUser.getPassword(), byEmail.get().getPassword());
    }

}