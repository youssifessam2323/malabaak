package io.joework.malabaakapi.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ComplexPasswordValidator.class)
public @interface ComplexPassword {
    int MIN_LENGTH = 8;
    String MESSAGE = "Password must be at least " + MIN_LENGTH + " characters long, include an uppercase letter, a lowercase letter, a number, a special character";

    int minLength() default MIN_LENGTH;
    String message() default MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
