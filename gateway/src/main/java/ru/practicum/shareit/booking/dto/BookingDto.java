package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.FieldDescription;

import java.time.LocalDateTime;

/**
 * add-bookings.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("id запроса на бронирование")
    Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Начало периода бронирования")
    LocalDateTime start;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Конец периода бронирования")
    LocalDateTime end;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Бронируемая вещь")
    ItemDto item;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Арендатор вещи")
    UserDto booker;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Статус запроса")
    String status;
}
