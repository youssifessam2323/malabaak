package io.joework.malabaakapi.service;

import io.joework.malabaakapi.exception.UserExistsException;
import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.VerificationLink;
import io.joework.malabaakapi.repository.UserRepository;
import io.joework.malabaakapi.repository.VerificationLinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
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
    void testSaveUser_shouldSaveUserSuccessfully_WhenUserIsValid() {
        when(passwordEncoder.encode(any(String.class))).thenReturn("password");
        when(userRepository.save(user)).thenReturn(user);

        classUnderTest.save(user);

        verify(userRepository, times(1)).save(user);

    }

    @Test
    void testSaveUser_ShouldThrowException_WhenUserIsExists(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserExistsException.class, () -> classUnderTest.save(user));
        verify(userRepository, never()).save(user);
    }

    @NullSource
    @ParameterizedTest
    void testEnableUser_shouldThrowIllegalArgumentException_WhenNullUser(User user){
        assertThrows(IllegalArgumentException.class, () -> classUnderTest.save(user));
        verify(userRepository, never()).save(user);
    }


    @Test
    void testEnableUser_shouldEnableUser_WhenHaveAValidVerificationLink() {
        VerificationLink verificationLink =
                new VerificationLink(0, UUID.randomUUID(), user, Instant.now().plus(10, ChronoUnit.MINUTES));
        when(verificationLinkRepository.findByVerificationToken(any(UUID.class))).thenReturn(Optional.of(verificationLink));

        boolean isEnabled = classUnderTest.enableUser(VERIFICATION_UUID);
        assertTrue(isEnabled);
        assertTrue(user.getIsEnabled());
    }

    @Test
    void testEnableUser_shouldNotEnableUser_WhenTokenIsExpired(){
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
    void testEnableUser_shouldNotEnableUser_WhenTokenNotFound(){

        when(verificationLinkRepository.findByVerificationToken(any(UUID.class))).thenReturn(Optional.empty());

        boolean isEnabled = classUnderTest.enableUser(VERIFICATION_UUID);

        assertFalse(isEnabled);
        verify(verificationLinkRepository, never()).delete(any(VerificationLink.class));
        assertFalse(user.getIsEnabled());
    }

    @NullSource
    @ParameterizedTest
    void testEnableUser_shouldThrowException_WhenTokenIsNull(String token){
        assertThrows(IllegalArgumentException.class,() -> classUnderTest.enableUser(token));
    }

    @Test
    void testCheckUserExists_ShouldReturnOptionOfUser_WhenValidEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> resultUser = classUnderTest.checkUserExists(user.getEmail());

        assertTrue(resultUser.isPresent());
        assertEquals(user.getEmail(), resultUser.get().getEmail());
    }

    @Test
    void testCheckUserExists_ShouldReturnOptionalOfEmpty_WhenInValidEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        Optional<User> resultUser = classUnderTest.checkUserExists(user.getEmail());

        assertFalse(resultUser.isPresent());
    }

    @Test
    void testCheckUserExists_ShouldThrowIllegalArgumentException_WhenNullEmail() {
        when(userRepository.findByEmail(null)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class,() -> classUnderTest.checkUserExists(null));
    }

}