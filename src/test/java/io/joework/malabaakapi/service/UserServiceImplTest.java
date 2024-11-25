package io.joework.malabaakapi.service;

import io.joework.malabaakapi.exception.UserExistsException;
import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.VerificationLink;
import io.joework.malabaakapi.repository.UserRepository;
import io.joework.malabaakapi.repository.VerificationLinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static io.joework.malabaakapi.fixtures.UserFixture.getUserEntityNewRecord;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    public static final String VERIFICATION_UUID = "20354d7a-e4fe-47af-8ff6-187bca92f3f9";
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private VerificationLinkRepository verificationLinkRepository;

    @InjectMocks
    private UserServiceImpl classUnderTest;

    private User user;

    @BeforeEach
    void setUp() {
        user = getUserEntityNewRecord();
    }

    @Test
    void saveUserSuccessfully() {
        when(passwordEncoder.encode(any(String.class))).thenReturn("password");
        when(userRepository.save(user)).thenReturn(user);

        classUnderTest.saveUser(user);

        verify(userRepository, times(1)).save(user);

    }

    @Test
    void testSavingExistingUser(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserExistsException.class, () -> classUnderTest.saveUser(user));
        verify(userRepository, never()).save(user);
    }

    @Test
    void testNullUser(){
        assertThrows(IllegalArgumentException.class, () -> classUnderTest.saveUser(null));
        verify(userRepository, never()).save(null);
    }


    @Test
    void testEnableUserWithAValidVerificationLink() {
        VerificationLink verificationLink = new VerificationLink(0, UUID.randomUUID(), user, Instant.now().plus(10, ChronoUnit.MINUTES));
        when(verificationLinkRepository.findByVerificationToken(any(UUID.class))).thenReturn(Optional.of(verificationLink));

        boolean isEnabled = classUnderTest.enableUser(VERIFICATION_UUID);
        assertTrue(isEnabled);
        assertEquals(user.getIsEnabled(), isEnabled);
    }

    @Test
    void testEnableUserWhenTokenIsExpired(){
        VerificationLink verificationLink = new VerificationLink(0, UUID.randomUUID(), user,
                Instant.now().minus(20, ChronoUnit.MINUTES)
                .plus(10, ChronoUnit.MINUTES));

        when(verificationLinkRepository.findByVerificationToken(any(UUID.class))).thenReturn(Optional.of(verificationLink));

        boolean isEnabled = classUnderTest.enableUser(VERIFICATION_UUID);

        assertFalse(isEnabled);
        assertFalse(user.getIsEnabled());
        verify(verificationLinkRepository, times(1)).delete(verificationLink);
    }

    @Test
    void testEnableUserWhenTokenNotFound(){

        when(verificationLinkRepository.findByVerificationToken(any(UUID.class))).thenReturn(Optional.empty());

        boolean isEnabled = classUnderTest.enableUser(VERIFICATION_UUID);

        assertFalse(isEnabled);
        verify(verificationLinkRepository, never()).delete(any(VerificationLink.class));
        assertFalse(user.getIsEnabled());
    }

    @Test
    void testEnableUserWhenTokenIsNull(){
        assertThrows(IllegalArgumentException.class,() -> classUnderTest.enableUser(null));
    }

    @Test
    void testCheckUserExistsWhenValidEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> resultUser = classUnderTest.checkUserExists(user.getEmail());

        assertTrue(resultUser.isPresent());
        assertEquals(user.getEmail(), resultUser.get().getEmail());
    }

    @Test
    void testCheckUserExistsWhenInValidEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        Optional<User> resultUser = classUnderTest.checkUserExists(user.getEmail());

        assertFalse(resultUser.isPresent());
    }

    @Test
    void testCheckUserExistsWhenNullEmail() {
        when(userRepository.findByEmail(null)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class,() -> classUnderTest.checkUserExists(null));
    }

}