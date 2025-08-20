package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemOnRequestDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription(value = "Уникальный идентификатор вещи")
    Long id;    // уникальный идентификатор вещи

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Название")
    String name;    // краткое название

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("id владельца вещи")
    Long ownerId;
}
