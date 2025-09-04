package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;

import static ru.practicum.shareit.util.NumericConstantsForRequest.LENGTH_DESCRIPTION_MAX;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewRequestItem {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotBlank(message = "Текст запроса не может быть пустым.", groups = Marker.OnCreate.class)
    @Size(max = LENGTH_DESCRIPTION_MAX, groups = Marker.OnCreate.class) // ограничим длину запроса пользователя
    @FieldDescription(value = "Текст запроса пользователя")
    String description;
}
