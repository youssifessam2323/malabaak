package io.joework.malabaakapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailServiceImpl classUnderTest;

    private static MimeMessage mimeMessage;
    @BeforeAll
    static void setup(){
        mimeMessage = mock(MimeMessage.class);
    }

    @Test
    void testSend_ShouldSendMail_WhenHaveValidParams(){
        //Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //Act & Assert
        assertDoesNotThrow(() -> classUnderTest.send("example@example.com", "Test Email", "body", true));
    }

    @Test
    void testSend_ShouldThrowIllegalArgumentException_WhenHavingToAsNull(){
        //Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> classUnderTest.send(null, "Test Email", "body", true));
    }

    @Test
    void testSend_ShouldThrowMessagingException_WhenHavingToAsEmptyString(){
        //Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //Act & Assert
        assertThrows(MessagingException.class, () -> classUnderTest.send("", "Test Email", "body", true));
    }

    @Test
    void testSend_ShouldIllegalArgumentException_WhenHavingNullSubject(){
        //Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> classUnderTest.send("example@example.com",null, "body", true));
    }

    @Test
    void testSend_ShouldSuccess_WhenHavingEmptySubject(){
        //Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //Act & Assert
        assertDoesNotThrow(() -> classUnderTest.send("example@example.com","", "body", true));
    }

    @Test
    void testSend_ShouldIllegalArgumentException_WhenHavingNullBody(){
        //Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> classUnderTest.send("example@example.com","hello", null, true));
    }

    @Test
    void testSend_ShouldSuccess_WhenHavingEmptyBody(){
        //Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //Act & Assert
        assertDoesNotThrow(() -> classUnderTest.send("example@example.com","hello", "", true));
    }
}