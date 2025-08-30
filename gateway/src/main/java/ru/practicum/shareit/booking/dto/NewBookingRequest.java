package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;
import ru.practicum.shareit.validation.StartEqualEnd;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@StartEqualEnd
public class NewBookingRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotNull(message = "При создании запроса должно быть указано начало периода аренды.",
            groups = Marker.OnCreate.class)
    @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом.")
    @FieldDescription("Дата и время начала бронирования")
    LocalDateTime start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotNull(message = "При создании запроса должен быть указан конец периода аренды.",
            groups = Marker.OnCreate.class)
    @Future(message = "Дата конца бронирования не может быть в прошлом / настоящем.")
    @FieldDescription("Дата и время конца бронирования")
    LocalDateTime end;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotNull(message = "При создании запроса должен быть указан id вещи.",
            groups = Marker.OnCreate.class)
    @FieldDescription("id вещи, которую пользователь бронирует")
    Long itemId;
}
