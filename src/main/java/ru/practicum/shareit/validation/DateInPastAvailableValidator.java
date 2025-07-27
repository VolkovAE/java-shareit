package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateInPastAvailableValidator implements ConstraintValidator<DateInPast, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) return true; // для этого случая, считаю валидацию успешной

        LocalDateTime localDateTimeNow = LocalDateTime.now();

        return value.isAfter(localDateTimeNow);
    }
}
