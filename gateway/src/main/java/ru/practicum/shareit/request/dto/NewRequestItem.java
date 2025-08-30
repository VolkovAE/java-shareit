package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewRequestItem {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotBlank(message = "Текст запроса не может быть пустым.", groups = Marker.OnCreate.class)
    @FieldDescription(value = "Текст запроса пользователя")
    String description;
}
