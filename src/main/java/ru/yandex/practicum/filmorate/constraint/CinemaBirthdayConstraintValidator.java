package ru.yandex.practicum.filmorate.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class CinemaBirthdayConstraintValidator implements ConstraintValidator<CinemaBirthdayConstraint, LocalDate> {
    private static final LocalDate BIRTHDAY_OF_CINEMA = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {
        // Отрицание необходимо, чтобы и сам ДР кино был валидным значением
        return !releaseDate.isBefore(BIRTHDAY_OF_CINEMA);
    }
}
