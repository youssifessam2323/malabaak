package io.joework.malabaakapi.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ComplexPasswordValidator implements ConstraintValidator<ComplexPassword, String> {
    private static final String PASSWORD_PATTERN = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    @Override
    public void initialize(ComplexPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String inputPassword, ConstraintValidatorContext constraintValidatorContext) {
        return pattern.matcher(inputPassword).matches();
    }
}
