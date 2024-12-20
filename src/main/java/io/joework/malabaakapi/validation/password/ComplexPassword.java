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
    int minimumAge() default 18;
    String message() default "Password must be at least 12 characters long, include an uppercase letter, a lowercase letter, a number, a special character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
