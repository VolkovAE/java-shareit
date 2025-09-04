package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

import static ru.practicum.shareit.util.NumericConstantsForRequest.LENGTH_DESCRIPTION_MAX;
import static ru.practicum.shareit.util.NumericConstantsForRequest.LENGTH_NAME_ITEM_MAX;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateItemRequest {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Size(max = LENGTH_NAME_ITEM_MAX)
    @FieldDescription("Название")
    String name;    // краткое название

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Size(max = LENGTH_DESCRIPTION_MAX)
    @FieldDescription("Описание")
    String description; // развёрнутое описание

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @FieldDescription("Доступность для аренды")
    Boolean available;  // статус о том, доступна или нет вещь для аренды (true - доступна, false - нет)
}
