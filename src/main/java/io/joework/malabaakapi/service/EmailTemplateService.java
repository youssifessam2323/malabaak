package io.joework.malabaakapi.service;

import java.util.Map;

public interface EmailTemplateService {
    String prepareEmailTemplate(String templateName, Map<String, Object> contextVariables);
}
