package com.anubhav.dev.store.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LowercaseValidator implements ConstraintValidator<Lowercase, String> {

    @Override
    public void initialize(Lowercase constraintAnnotation) {
        // No initialization required
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (char c : value.toCharArray()) {
            if (Character.isLetter(c) && Character.isUpperCase(c)) {
                return false;
            }
        }

        return true;
    }
}
