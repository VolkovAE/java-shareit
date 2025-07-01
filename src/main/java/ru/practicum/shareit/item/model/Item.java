package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;

/**
 * Item.
 */
@Data
@EqualsAndHashCode(of = {"id", "name", "owner"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Null(message = "При создании вещи id формируется автоматически.", groups = Marker.OnCreate.class)
    @NotNull(message = "При обновлении данных о вещи должен быть указан её id.",
            groups = {Marker.OnUpdate.class, Marker.OnDelete.class})
    @FieldDescription(value = "Уникальный идентификатор вещи", changeByCopy = false)
    Long id;

    @NotBlank(message = "Название вещи не может быть пустым.", groups = Marker.OnCreate.class)
    @FieldDescription("Название")
    String name;

    @NotBlank(message = "При создании вещи должна быть указано описание.", groups = Marker.OnCreate.class)
    @FieldDescription("Описание")
    String description;

    @NotNull(message = "При создании вещи должна быть указана доступность для аренды.",
            groups = Marker.OnCreate.class)
    @FieldDescription("Доступность для аренды")
    Boolean available;  // статус о том, доступна или нет вещь для аренды (true - доступна, false - нет)

    @FieldDescription(value = "Владелец", changeByCopy = false)
    User owner; // владелец вещи

    @FieldDescription("Запрос на вещь другого пользователя")
    ItemRequest request;    // если вещь была создана по запросу другого пользователя, то в этом
    // поле будет храниться ссылка на соответствующий запрос
}
