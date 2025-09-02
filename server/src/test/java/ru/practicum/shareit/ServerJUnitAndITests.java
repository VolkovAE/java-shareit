package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemDBServiceImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserDBServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модуль Unit-тестов и интеграционных тестов сервисов модуля server.
 */
@Slf4j
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:ShareIt",
        "spring.datasource.driverClassName=org.h2.Driver"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerJUnitAndITests {
    static final int LENGTH_NAME_NEW_USER = 10;
    static final int LENGTH_EMAIL_NEW_USER = 15;
    static final String NAME_DOMAIN = "yandex.ru";
    static final Long ID_USER_NO_CORRECT = 123456789L;
    static final int LENGTH_NAME_NEW_ITEM = 10;
    static final int LENGTH_DESCR_NEW_ITEM = 50;
    static final Long ID_ITEM_NO_CORRECT = 123456789L;

    @Autowired
    private final UserDBServiceImpl userService;

    @Autowired
    private final ItemDBServiceImpl itemService;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final BookingServiceImpl bookingService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testServiceUser() {
        NewUserRequest userRequest = getNewUserRequest();

        // Проверяем запись нового пользователя в БД.
        UserDto userDto = userService.add(userRequest);

        assertEquals(userRequest.getName(), userDto.getName(), "Ошибка записи нового пользователя в БД: Не верно записано его имя.");
        assertEquals(userRequest.getEmail(), userDto.getEmail(), "Ошибка записи нового пользователя в БД: Не верно записан его имэйл.");
        assertNotNull(userDto.getId(), "Ошибка записи нового пользователя в БД: Возвращено NULL в ID.");

        // Проверяем получение данных существующего пользователя по его id.
        UserDto userDtoById = userService.getById(userDto.getId());
        assertEquals(userDto.getName(), userDtoById.getName(), "Ошибка получения данных пользователя по id: Не верно записано его имя.");
        assertEquals(userDto.getEmail(), userDtoById.getEmail(), "Ошибка получения данных пользователя по id: Не верно записан его имэйл.");
        assertNotNull(userDtoById.getId(), "Ошибка получения данных пользователя по id: Возвращено NULL в ID.");

        // Проверяем отсутствие пользователя с несуществующим id.
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            userService.getById(ID_USER_NO_CORRECT);
        }, "Не выброшено исключение при попытке получить пользователя с не существующим id.");

        assertEquals("Пользователь с id = " + ID_USER_NO_CORRECT + " не найден.", thrown.getMessage(),
                "Не верный текст исключения при получении пользователя по не существующему id.");

        // Проверяем получение списка пользователей.
        Collection<UserDto> userDtoCollection = userService.findAll(0, 1000);   // данный размер страницы позволит содержать добавленного пользователя
        ArrayList<UserDto> userDtoArrayList = userDtoCollection.stream()
                .filter(userDtoCur -> userDtoCur.getId().equals(userDto.getId()))
                .collect(Collectors.toCollection(ArrayList::new));

        assertEquals(1, userDtoArrayList.size(), "Ошибка при получении страницы пользователей: Не найден пользователь по id.");

        assertEquals(userDto.getName(), userDtoArrayList.getFirst().getName(), "Ошибка при получении страницы пользователей: Не верно записано его имя.");
        assertEquals(userDto.getEmail(), userDtoArrayList.getFirst().getEmail(), "Ошибка при получении страницы пользователей: Не верно записан его имэйл.");
        assertNotNull(userDto.getId(), "Ошибка при получении страницы пользователей: Возвращено NULL в ID.");

        // Проверяем обновление данных пользователя в БД.
        UpdateUserRequest updateUserRequest = getUpdateUserRequest();

        UserDto userDtoUpdate = userService.update(userDto.getId(), updateUserRequest);

        userDtoById = userService.getById(userDto.getId());
        assertEquals(updateUserRequest.getName(), userDtoById.getName(), "Ошибка при обновлении данных пользователя по id: Не верно записано его имя.");
        assertEquals(updateUserRequest.getEmail(), userDtoById.getEmail(), "Ошибка при обновлении данных пользователя по id: Не верно записан его имэйл.");
        assertNotNull(userDtoById.getId(), "Ошибка получения данных пользователя по id: Возвращено NULL в ID.");

        // Проверяем удаление пользователя из БД.
        UserDto userDtoDelete = userService.delete(userDto.getId());

        thrown = assertThrows(NotFoundException.class, () -> {
            userService.getById(userDtoDelete.getId());
        }, "Не выброшено исключение при попытке получить удаленного пользователя.");
    }

    private NewUserRequest getNewUserRequest() {
        String name = RandomStringUtils.randomAlphabetic(LENGTH_NAME_NEW_USER);

        StringBuilder builderEmail = new StringBuilder();
        builderEmail.append(RandomStringUtils.randomAlphabetic(LENGTH_EMAIL_NEW_USER));
        builderEmail.append("@");
        builderEmail.append(NAME_DOMAIN);

        NewUserRequest userRequest = new NewUserRequest();
        userRequest.setName(name);
        userRequest.setEmail(builderEmail.toString());

        return userRequest;
    }

    private UpdateUserRequest getUpdateUserRequest() {
        NewUserRequest newUserRequest = getNewUserRequest();

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName(newUserRequest.getName());
        updateUserRequest.setEmail(newUserRequest.getEmail());

        return updateUserRequest;
    }

    @Test
    public void testServiceItem() {
        NewUserRequest userRequest = getNewUserRequest();

        UserDto userDto = userService.add(userRequest);

        // Тестируем запись новой вещи в БД без запроса.
        NewItemRequest itemRequest = getNewItemRequest(true);

        ItemDto itemDto = itemService.add(itemRequest, userDto.getId());
        assertEquals(itemRequest.getName(), itemDto.getName(), "Ошибка при записи новой вещи: Не верно записано имя.");
        assertEquals(itemRequest.getDescription(), itemDto.getDescription(), "Ошибка при записи новой вещи: Не верно записано наименование.");
        assertEquals(itemRequest.getAvailable(), itemDto.getAvailable(), "Ошибка при записи новой вещи: Не верно записана доступность аренды.");
        assertNotNull(itemDto.getId(), "Ошибка при записи новой вещи: Возвращено NULL в ID.");

        // Тестируем получение вещи по id.
        ItemWithDateDto itemDtoById = itemService.getById(itemDto.getId(), userDto.getId());
        assertEquals(itemDto.getName(), itemDtoById.getName(), "Ошибка получения данных вещи по id: Не верно записано имя.");
        assertEquals(itemDto.getDescription(), itemDtoById.getDescription(), "Ошибка получения данных вещи по id: Не верно записано наименование.");
        assertEquals(itemDto.getAvailable(), itemDtoById.getAvailable(), "Ошибка получения данных вещи по id: Не верно записана доступность аренды.");
        assertNotNull(itemDto.getId(), "Ошибка получения данных вещи по id: Возвращено NULL в ID.");

        // Проверяем отсутствие вещи с несуществующим id.
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            itemService.getById(ID_ITEM_NO_CORRECT, ID_USER_NO_CORRECT);
        }, "Не выброшено исключение при попытке получить вещь с не существующим id и не существующим пользователем.");

        // Проверяем получение вещей пользователя с id.
        Collection<ItemWithDateDto> itemByUser = itemService.findAll(userDto.getId());

        assertEquals(1, itemByUser.size(), "Ошибка в получении вещей пользователя.");

        // Проверяем поиск вещи по фрагменту.
        String textSearch = itemDto.getDescription().substring(1);  // пропускаем первый символ
        Collection<ItemDto> itemByText = itemService.findAllByText(textSearch);

        assertEquals(1, itemByUser.size(), "Ошибка в получении вещей по подстроке.");

        // Проверяем обновление данных по вещи.
        UpdateItemRequest updateItemRequest = getUpdateItemRequest(true);

        ItemDto itemDtoUpdate = itemService.update(itemDto.getId(), updateItemRequest, userDto.getId());
        assertEquals(updateItemRequest.getName(), itemDtoUpdate.getName(), "Ошибка обновления данных вещи по id: Не верно записано имя.");
        assertEquals(updateItemRequest.getDescription(), itemDtoUpdate.getDescription(), "Ошибка обновления данных вещи по id: Не верно записано наименование.");
        assertEquals(updateItemRequest.getAvailable(), itemDtoUpdate.getAvailable(), "Ошибка обновления данных вещи по id: Не верно записана доступность аренды.");

        // Проверяем, что пользователь является владельцем вещи.
        Item item = itemRepository.findById(itemDto.getId()).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemDto.getId() + " не найдена.", log));
        Boolean isOwner = itemService.isOwner(item, userDto.getId());

        assertTrue(isOwner, "Ошибка в подтверждении владельца вещи.");

        // Проверяем удаление данных по вещи.
        ItemDto itemDtoDelete = itemService.delete(itemDto.getId(), userDto.getId());

        thrown = assertThrows(NotFoundException.class, () -> {
            itemService.getById(itemDtoDelete.getId(), userDto.getId());
        }, "Не выброшено исключение при попытке получить удаленную вещь.");
    }

    private NewItemRequest getNewItemRequest(Boolean available) {
        NewItemRequest itemRequest = new NewItemRequest();
        itemRequest.setName(RandomStringUtils.randomAlphabetic(LENGTH_NAME_NEW_ITEM));
        itemRequest.setDescription(RandomStringUtils.randomAlphabetic(LENGTH_DESCR_NEW_ITEM));
        itemRequest.setAvailable(available);

        return itemRequest;
    }

    private UpdateItemRequest getUpdateItemRequest(Boolean available) {
        NewItemRequest newItemRequest = getNewItemRequest(available);

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName(newItemRequest.getName());
        updateItemRequest.setDescription(newItemRequest.getDescription());
        updateItemRequest.setAvailable(newItemRequest.getAvailable());

        return updateItemRequest;
    }

    @Test
    public void testServiceBooking() {
        // Подготовим владельца вещи.
        NewUserRequest userRequestOwner = getNewUserRequest();

        UserDto userDtoOwner = userService.add(userRequestOwner);

        // Подготовим вещь.
        NewItemRequest itemRequest = getNewItemRequest(true);

        ItemDto itemDto = itemService.add(itemRequest, userDtoOwner.getId());

        // Подготовим букера.
        NewUserRequest userRequestBooker = getNewUserRequest();

        UserDto userDtoBooker = userService.add(userRequestBooker);

        // Проверяем добавление заявки.
        NewBookingRequest newBookingRequest = getNewBookingRequest(itemDto.getId());

        BookingDto bookingDto = bookingService.add(newBookingRequest, userDtoBooker.getId());

        assertEquals(newBookingRequest.getStart(), bookingDto.getStart(), "Ошибка при записи новой заявки на аренду: Не верно записано начало периода.");
        assertEquals(newBookingRequest.getEnd(), bookingDto.getEnd(), "Ошибка при записи новой заявки на аренду: Не верно записано окончание периода.");
        assertEquals(newBookingRequest.getItemId(), bookingDto.getItem().getId(), "Ошибка при записи новой заявки на аренду: В заявке указана не верная вещь.");
        assertEquals(userDtoBooker.getId(), bookingDto.getBooker().getId(), "Ошибка при записи новой заявки на аренду: В заявке указан не верно арендатор.");
        assertEquals(StatusBooking.WAITING, StatusBooking.fromString(bookingDto.getStatus()), "Ошибка при записи новой заявки на аренду: В заявке указан не верно статус.");
        assertNotNull(bookingDto.getId(), "Ошибка при записи новой заявки на аренду: Возвращено NULL в ID.");

        // Проверяем утверждение заявки владельцем вещи.


        //        BookingDto approve(Long bookingId, Long userId, boolean approval);


        // Проверяем поиск заявки по id.

        //        BookingDto findById(Long bookingId, Long userId);


        // Проверяем поиск заявок арендатора.
        //        Collection<BookingDto> findByBookerOrOwner(Long userId, String state, Boolean isOwner);

        // Проверяем поиск заявок на вещи владельца.

        //        Collection<BookingDto> findByBookerOrOwner(Long userId, String state, Boolean isOwner);

    }

    private NewBookingRequest getNewBookingRequest(Long itemId) {
        NewBookingRequest newBookingRequest = new NewBookingRequest();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusSeconds(5);
        LocalDateTime end = start.plusSeconds(10);

        newBookingRequest.setStart(start);
        newBookingRequest.setEnd(end);
        newBookingRequest.setItemId(itemId);

        return newBookingRequest;
    }

//    // Проверяем добавление комментария (item).
//    itemRequest = getNewItemRequest(true);
//
//    itemDto = itemService.add(itemRequest, userDto.getId());

    //        CommentDto addComment(Long itemId, Long userId, NewCommentRequest commentRequest);

}
