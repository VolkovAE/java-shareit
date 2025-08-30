package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.StartEqualEnd;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@StartEqualEnd
public class NewBookingRequest {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @FieldDescription("Дата и время начала бронирования")
    LocalDateTime start;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @FieldDescription("Дата и время конца бронирования")
    LocalDateTime end;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @FieldDescription("id вещи, которую пользователь бронирует")
    Long itemId;
}
