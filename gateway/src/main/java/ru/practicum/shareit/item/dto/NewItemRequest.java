package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;

import static ru.practicum.shareit.util.NumericConstantsForRequest.LENGTH_DESCRIPTION_MAX;
import static ru.practicum.shareit.util.NumericConstantsForRequest.LENGTH_NAME_ITEM_MAX;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewItemRequest {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotBlank(message = "Название вещи не может быть пустым.", groups = Marker.OnCreate.class)
    @Size(max = LENGTH_NAME_ITEM_MAX, groups = Marker.OnCreate.class)
    @FieldDescription("Название")
    String name;    // краткое название

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotBlank(message = "При создании вещи должна быть указано описание.", groups = Marker.OnCreate.class)
    @Size(max = LENGTH_DESCRIPTION_MAX, groups = Marker.OnCreate.class)
    @FieldDescription("Описание")
    String description; // развёрнутое описание

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotNull(message = "При создании вещи должна быть указана доступность для аренды.",
            groups = Marker.OnCreate.class)
    @FieldDescription("Доступность для аренды")
    Boolean available;  // статус о том, доступна или нет вещь для аренды (true - доступна, false - нет)

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @FieldDescription("Запрос на вещь, которую размещаем")
    Long requestId;
}
