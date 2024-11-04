package io.joework.malabaakapi.service.mail;

import java.util.Map;

public interface EmailTemplateService {
    String prepareEmailTemplate(String templateName, Map<String, Object> contextVariables);
}
