package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

/**
 * ItemDto.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription(value = "Уникальный идентификатор вещи")
    Long id;    // уникальный идентификатор вещи

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Название")
    String name;    // краткое название

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Описание")
    String description; // развёрнутое описание

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Доступность для аренды")
    Boolean available;  // статус о том, доступна или нет вещь для аренды (true - доступна, false - нет)
}
