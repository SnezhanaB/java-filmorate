package ru.yandex.practicum.filmorate.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthdayConstraintValidator implements ConstraintValidator<BirthdayConstraint, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext context) {
        return !birthday.isAfter(LocalDate.now());
    }
}
