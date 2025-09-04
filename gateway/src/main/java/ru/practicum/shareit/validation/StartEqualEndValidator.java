package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

public class StartEqualEndValidator implements ConstraintValidator<StartEqualEnd, NewBookingRequest> {
    @Override
    public boolean isValid(NewBookingRequest value, ConstraintValidatorContext context) {
        if (value.getStart() == null || value.getEnd() == null)
            return true; //для этого случая, считаю валидацию успешной

        return !(value.getEnd().equals(value.getStart()));
    }
}
