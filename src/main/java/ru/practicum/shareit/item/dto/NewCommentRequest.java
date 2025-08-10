package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentRequest {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Текст комментария не может быть пустым.", groups = Marker.OnCreate.class)
    @FieldDescription("Текст комментария")
    String text;
}
