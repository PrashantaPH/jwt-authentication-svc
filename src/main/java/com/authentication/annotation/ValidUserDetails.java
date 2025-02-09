package com.authentication.annotation;

import com.authentication.validator.UserDetailsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) /* ElementType.PARAMETER - method level and ElementType.Field - class field level */
@Constraint(validatedBy = UserDetailsValidator.class)
public @interface ValidUserDetails {

    String message() default "Invalid user details request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
