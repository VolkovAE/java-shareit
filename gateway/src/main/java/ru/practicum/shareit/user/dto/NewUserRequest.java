package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;

@Data
@EqualsAndHashCode(of = {"email"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Email(message = "Email is not valid")
    @NotBlank(message = "Электронная почта не может быть пустой.", groups = Marker.OnCreate.class)
    @FieldDescription("Электронная почта")
    String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @FieldDescription("Имя для отображения")
    String name;
}
