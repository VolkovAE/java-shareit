package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;

/**
 * Item.
 */
@Entity
@Table(name = "Items")
@Setter
@Getter
@EqualsAndHashCode(of = {"id", "name", "owner"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "При создании вещи id формируется автоматически.", groups = Marker.OnCreate.class)
    @NotNull(message = "При обновлении данных о вещи должен быть указан её id.",
            groups = {Marker.OnUpdate.class, Marker.OnDelete.class})
    @FieldDescription(value = "Уникальный идентификатор вещи", changeByCopy = false)
    Long id;

    @Column(name = "name")
    @NotBlank(message = "Название вещи не может быть пустым.", groups = Marker.OnCreate.class)
    @FieldDescription("Название")
    String name;

    @Column(name = "description")
    @NotBlank(message = "При создании вещи должна быть указано описание.", groups = Marker.OnCreate.class)
    @FieldDescription("Описание")
    String description;

    @Column(name = "is_available")
    @NotNull(message = "При создании вещи должна быть указана доступность для аренды.",
            groups = Marker.OnCreate.class)
    @FieldDescription("Доступность для аренды")
    Boolean available;  // статус о том, доступна или нет вещь для аренды (true - доступна, false - нет)

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    @FieldDescription(value = "Владелец", changeByCopy = false)
    User owner; // владелец вещи

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", nullable = true)
    @FieldDescription("Запрос на вещь другого пользователя")
    ItemRequest request;    // если вещь была создана по запросу другого пользователя, то в этом
    // поле будет храниться ссылка на соответствующий запрос
}
