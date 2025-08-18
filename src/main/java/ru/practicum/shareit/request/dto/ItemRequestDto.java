package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

import java.time.LocalDateTime;

/**
 * Sprint add-item-requests.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription(value = "id запроса на вещь")
    Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription(value = "Текст запроса")
    String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription(value = "Дата и время создания запроса")
    LocalDateTime created;
}
