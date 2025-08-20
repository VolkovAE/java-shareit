package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;

/**
 * User.
 */
@Entity
@Table(name = "Users")
@Setter
@Getter
@EqualsAndHashCode(of = {"email"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "При создании пользователя id формируется автоматически.", groups = Marker.OnCreate.class)
    @NotNull(message = "При обновлении данных о пользователе должен быть указан его id.",
            groups = {Marker.OnUpdate.class, Marker.OnDelete.class})
    @FieldDescription(value = "Уникальный идентификатор пользователя", changeByCopy = false)
    Long id;

    @Column(name = "email")
    @Email(message = "Email is not valid")
    @NotBlank(message = "Электронная почта не может быть пустой.", groups = Marker.OnCreate.class)
    @FieldDescription("Электронная почта")
    String email;   // адрес электронной почты (два пользователя не могут иметь одинаковый адрес электронной почты)

    @Column(name = "name")
    @FieldDescription("Имя для отображения")
    String name;    // имя или логин пользователя
}
