package ru.practicum.shareit.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

import java.util.Map;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;
import static ru.practicum.shareit.util.Utils.addParameterRequest;

@Service
@Qualifier("UserClient")
@CacheConfig(cacheNames = {COMMON_CACHE})
public class UserClient extends BaseClient {
    private static final String API_PREFIX = API_PREFIX_USER;

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    @Autowired
    public UserClient(@Value(NAME_PARAMETER_SERVER_ADDRESS) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    // @Cacheable - убрал из-за тестов, т.к. второй запрос с таким же имэйл возвращает 200, а должен 409 или 500 по тесту.
    public ResponseEntity<Object> add(NewUserRequest userRequest) {
        log.info("Передан на сервер запрос: Добавить нового пользователя с email {}, name {}.", userRequest.getEmail(), userRequest.getName());

        return post("", userRequest);
    }

    @Cacheable
    public ResponseEntity<Object> getById(Long userId) {
        log.info("Передан на сервер запрос: Получить данные пользователя с id {}.", userId);

        return get(SEPARATOR + userId, userId);
    }

    @Cacheable
    public ResponseEntity<Object> findAll(int page, int count) {
        log.info("Передан на сервер запрос: Получить список пользователей по странично. Индекс страницы {} с количеством пользователей на странице {}.", page, count);

        Map<String, Object> parameters = Map.of(
                REQUEST_PARAM_PAGE, page,
                REQUEST_PARAM_COUNT, count);

        StringBuilder pathBuilder = new StringBuilder();
        addParameterRequest(pathBuilder, REQUEST_PARAM_PAGE);
        addParameterRequest(pathBuilder, REQUEST_PARAM_COUNT);

        return get(pathBuilder.toString(), parameters);
    }

    @Cacheable
    public ResponseEntity<Object> update(Long userId, UpdateUserRequest userRequest) {
        log.info("Передан на сервер запрос: Обновить данные пользователя с id {}. Установить значения email {}, name {}.",
                userId, userRequest.getEmail(), userRequest.getName());

        return patch(SEPARATOR + userId, userRequest);
    }

    @Cacheable
    public ResponseEntity<Object> delete(Long userId) {
        log.info("Передан на сервер запрос: Удалить данные пользователя с id {}.", userId);

        return delete(SEPARATOR + userId);
    }
}
