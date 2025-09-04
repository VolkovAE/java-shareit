package ru.practicum.shareit.request.service;

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
import ru.practicum.shareit.request.dto.NewRequestItem;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;
import static ru.practicum.shareit.util.Utils.addPathParameter;

@Service
@Qualifier("RequestClient")
@CacheConfig(cacheNames = {COMMON_CACHE})
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = API_PREFIX_REQUEST;

    private static final Logger log = LoggerFactory.getLogger(RequestClient.class);

    @Autowired
    public RequestClient(@Value(NAME_PARAMETER_SERVER_ADDRESS) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    @Cacheable
    public ResponseEntity<Object> add(NewRequestItem requestItem, Long userId) {
        log.info("Передан на сервер запрос: Добавить новый запрос на вещь {} от пользователя с id {}.", requestItem.toString(), userId);

        return post("", userId, requestItem);
    }

    @Cacheable
    public ResponseEntity<Object> findAllMy(Long userId) {
        log.info("Передан на сервер запрос: Получить все запросы на вещи пользователя с id {}.", userId);

        return get("", userId);
    }

    @Cacheable
    public ResponseEntity<Object> findAllOther(Long userId) {
        log.info("Передан на сервер запрос: Получить все запросы на вещи владельца с id {}.", userId);

        StringBuilder pathBuilder = new StringBuilder();
        addPathParameter(pathBuilder, PATH_VARIABLE_ALL);

        return get(pathBuilder.toString(), userId);
    }

    @Cacheable
    public ResponseEntity<Object> findById(Long requestId) {
        log.info("Передан на сервер запрос: Получить данные по запросу на вещь с id {}.", requestId);

        return get(SEPARATOR + requestId);
    }
}
