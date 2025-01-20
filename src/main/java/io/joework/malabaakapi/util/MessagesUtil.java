package io.joework.malabaakapi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessagesUtil {
    private final MessageSource messageSource;

    public String getMessage(String code) {
        return messageSource.getMessage(code,
                null,
                Locale.getDefault());
    }

    public String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code,
                null,
                locale);
    }

}
