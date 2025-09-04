package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentRequest {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @FieldDescription("Текст комментария")
    String text;
}
