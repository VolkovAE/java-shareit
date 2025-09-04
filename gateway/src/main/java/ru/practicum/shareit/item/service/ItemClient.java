package ru.practicum.shareit.item.service;

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
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Map;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;
import static ru.practicum.shareit.util.Utils.addParameterRequest;
import static ru.practicum.shareit.util.Utils.addPathParameter;

@Service
@Qualifier("ItemClient")
@CacheConfig(cacheNames = {COMMON_CACHE})
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = API_PREFIX_ITEM;

    private static final Logger log = LoggerFactory.getLogger(ItemClient.class);

    @Autowired
    public ItemClient(@Value(NAME_PARAMETER_SERVER_ADDRESS) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    @Cacheable
    public ResponseEntity<Object> add(NewItemRequest itemRequest, Long userId) {
        log.info("Передан на сервер запрос: Добавить новую вещь с параметрами {} пользователю с id {}.", itemRequest.toString(), userId);

        return post("", userId, itemRequest);
    }

    @Cacheable
    public ResponseEntity<Object> getById(Long itemId, Long userId) {
        log.info("Передан на сервер запрос: Получить данные вещи с id {} пользователя с id {}.", itemId, userId);

        return get(SEPARATOR + itemId, userId);
    }

    @Cacheable
    public ResponseEntity<Object> findAll(Long userId) {
        log.info("Передан на сервер запрос: Получить список вещей пользователя с id {}.", userId);

        return get("", userId);
    }

    @Cacheable
    public ResponseEntity<Object> findAllByText(String textSearch) {
        log.info("Передан на сервер запрос: Получить список вещей по подстроке {}.", textSearch);

        Map<String, Object> parameters = Map.of(REQUEST_PARAM_TEXT, textSearch);

        StringBuilder pathBuilder = new StringBuilder();
        addPathParameter(pathBuilder, PATH_VARIABLE_SEARCH);
        addParameterRequest(pathBuilder, REQUEST_PARAM_TEXT);

        return get(pathBuilder.toString(), parameters);
    }

    @Cacheable
    public ResponseEntity<Object> update(Long itemId, UpdateItemRequest itemRequest, Long userId) {
        log.info("Передан на сервер запрос: Обновить данные вещи с id {} владельца с id {} на значения {}.", itemId, userId, itemRequest.toString());

        return patch(SEPARATOR + itemId, userId, itemRequest);
    }

    @Cacheable
    public ResponseEntity<Object> delete(Long itemId, Long userId) {
        log.info("Передан на сервер запрос: Удалить данные вещи с id {} владельца с id {}.", itemId, userId);

        return delete(SEPARATOR + itemId, userId);
    }

    @Cacheable
    public ResponseEntity<Object> addComment(Long itemId, Long userId, NewCommentRequest commentRequest) {
        log.info("Передан на сервер запрос: Добавить комментарий {} к вещи с id {} от пользователя (арендовавшего) с id {}.",
                commentRequest.getText(), itemId, userId);

        StringBuilder pathBuilder = new StringBuilder();
        addPathParameter(pathBuilder, itemId.toString());
        addPathParameter(pathBuilder, PATH_VARIABLE_COMMENT);

        return post(pathBuilder.toString(), userId, commentRequest);
    }
}
