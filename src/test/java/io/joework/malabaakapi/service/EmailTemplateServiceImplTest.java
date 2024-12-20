package io.joework.malabaakapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailTemplateServiceImplTest {

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailTemplateServiceImpl classUnderTest;

    @Test
    void testPrepareEmailTemplate_ShouldSuccess_WhenValidParameters() {
        when(templateEngine.process(any(String.class),any(Context.class))).thenReturn("email body");

        String emailTemplate = classUnderTest.prepareEmailTemplate("templateName", Map.of("x", "y"));

        assertNotNull(emailTemplate);
        assertTrue(emailTemplate.contains("email body"));
    }

    @Test
    void testPrepareEmailTemplate_ShouldFail_WhenTemplateNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> classUnderTest.prepareEmailTemplate(null, Map.of("x", "y")));
    }

}