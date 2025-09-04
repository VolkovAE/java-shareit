package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ItemDto with date.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemWithDateDto {
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Дата последнего бронирования")
    LocalDateTime lastBooking;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Дата следующего бронирования")
    LocalDateTime nextBooking;

    @JsonProperty(value = "comments", access = JsonProperty.Access.READ_ONLY)
    @FieldDescription("Список комментариев от пользователей, которые арендовали эту вещь.")
    List<CommentDto> commentDtoList;
}
