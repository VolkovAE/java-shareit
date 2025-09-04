package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewRequestItem {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @FieldDescription(value = "Текст запроса пользователя")
    String description;
}
