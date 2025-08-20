package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateItemRequest {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @FieldDescription("Название")
    String name;    // краткое название

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @FieldDescription("Описание")
    String description; // развёрнутое описание

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @FieldDescription("Доступность для аренды")
    Boolean available;  // статус о том, доступна или нет вещь для аренды (true - доступна, false - нет)
}
