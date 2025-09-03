package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @FieldDescription(value = "id комментария")
    Long id;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @FieldDescription(value = "Текст комментария")
    String text;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @FieldDescription(value = "Вещь, которая была арендована пользователем, к которой оставляется комментарий")
    ItemDto item;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @FieldDescription(value = "Имя автора комментария")
    String authorName;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @FieldDescription(value = "Дата и время создания комментария")
    LocalDateTime created;
}
