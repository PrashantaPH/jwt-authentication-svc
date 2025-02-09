package com.authentication.validator;

import com.authentication.annotation.ValidUserDetails;
import com.authentication.model.UserDetails;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.authentication.util.Constants.*;

public class UserDetailsValidator implements ConstraintValidator<ValidUserDetails, UserDetails> {

    @Override
    public boolean isValid(UserDetails userDetails, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println(userDetails);
        List<String> fieldList = Arrays.asList(
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getUserName(),
                userDetails.getPassword(),
                userDetails.getEmailId(),
                userDetails.getRole());

        boolean areFieldsValid = fieldList.stream().allMatch(this::isFieldValid);
        if (!areFieldsValid) {
            customMessageForValidation(constraintValidatorContext, MISSING_PARAMETER, "One or more required fields are empty");
            return false;
        }

        if (userDetails.getRoles().isEmpty()) {
            customMessageForValidation(constraintValidatorContext, MISSING_PARAMETER, "One or more required fields are empty");
            return false;
        }

        if (!Pattern.matches(EMAIL_REGEX, userDetails.getEmailId())) {
            customMessageForValidation(constraintValidatorContext, INVALID_EMAIL_FORMAT, "Invalid email format");
            return false;
        }

        return true;
    }

    private boolean isFieldValid(String field) {
        return field != null && !field.trim().isEmpty();
    }

    private void customMessageForValidation(ConstraintValidatorContext constraintContext, String errorCode, String message) {
        constraintContext.disableDefaultConstraintViolation();
        constraintContext.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(errorCode)
                .addConstraintViolation();
    }

}
