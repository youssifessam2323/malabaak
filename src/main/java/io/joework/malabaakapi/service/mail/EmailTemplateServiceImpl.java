package io.joework.malabaakapi.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final SpringTemplateEngine templateEngine;

    @Override
    public String prepareEmailTemplate(String templateName, Map<String, Object> contextVariables) {
        Context context = new Context();
        context.setVariables(contextVariables);

        return templateEngine.process(templateName, context);
    }
}
