package com.unicornstudy.singleshop.exception.items.validator;

import com.unicornstudy.singleshop.items.domain.ChildCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ChildCategoryValidator implements ConstraintValidator<ValidChildCategory, String> {

    @Override
    public void initialize(ValidChildCategory constraintAnnotation) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(ChildCategory.values())
                .anyMatch(category -> category.name().equals(value));
    }
}
