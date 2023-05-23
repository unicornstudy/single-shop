package com.unicornstudy.singleshop.exception.items.validator;

import com.unicornstudy.singleshop.items.domain.ParentCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ParentCategoryValidator implements ConstraintValidator<ValidParentCategory, String> {
    @Override
    public void initialize(ValidParentCategory constraintAnnotation) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(ParentCategory.values())
                .anyMatch(category -> category.name().equals(value));
    }
}
