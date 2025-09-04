package ru.practicum.shareit.booking.service;

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
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

import static ru.practicum.shareit.util.StringConstantsForRequest.*;
import static ru.practicum.shareit.util.Utils.addParameterRequest;
import static ru.practicum.shareit.util.Utils.addPathParameter;

@Service
@Qualifier("BookingClient")
@CacheConfig(cacheNames = {COMMON_CACHE})
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = API_PREFIX_BOOKING;

    private static final Logger log = LoggerFactory.getLogger(BookingClient.class);

    @Autowired
    public BookingClient(@Value(NAME_PARAMETER_SERVER_ADDRESS) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    @Cacheable
    public ResponseEntity<Object> add(NewBookingRequest newBookingRequest, Long userId) {
        log.info("Передан на сервер запрос: Добавить заявку на аренду {} от пользователя с id {}.", newBookingRequest.toString(), userId);

        return post("", userId, newBookingRequest);
    }

    @Cacheable
    public ResponseEntity<Object> approve(Long bookingId, Long userId, Boolean approval) {
        if (approval)
            log.info("Передан на сервер запрос: Утвердить заявку на аренду с id {} владельцем с id {}.", bookingId, userId);
        else
            log.info("Передан на сервер запрос: Отказать по заявке на аренду с id {} владельцем с id {}.", bookingId, userId);

        Map<String, Object> parameters = Map.of(REQUEST_PARAM_APPROVED, approval);

        StringBuilder pathBuilder = new StringBuilder();
        addPathParameter(pathBuilder, bookingId.toString());
        addParameterRequest(pathBuilder, REQUEST_PARAM_APPROVED);

        return patch(pathBuilder.toString(), userId, parameters);
    }

    @Cacheable
    public ResponseEntity<Object> findById(Long bookingId, Long userId) {
        log.info("Передан на сервер запрос: Получить данные по заявке на аренду {}.", bookingId);

        return get(SEPARATOR + bookingId, userId);
    }

    @Cacheable
    public ResponseEntity<Object> findByBooker(Long userId, String state) {
        log.info("Передан на сервер запрос: Получить заявки на аренду от пользователя с id {} в состоянии {}.", userId, state);

        Map<String, Object> parameters = Map.of(REQUEST_PARAM_STATE, state);

        StringBuilder pathBuilder = new StringBuilder();
        addParameterRequest(pathBuilder, REQUEST_PARAM_STATE);

        return get(pathBuilder.toString(), userId, parameters);
    }

    @Cacheable
    public ResponseEntity<Object> findByOwner(Long userId, String state) {
        log.info("Передан на сервер запрос: Получить заявки на аренду вещей владельца с id {} в состоянии {}.", userId, state);

        Map<String, Object> parameters = Map.of(REQUEST_PARAM_STATE, state);

        StringBuilder pathBuilder = new StringBuilder();
        addPathParameter(pathBuilder, PATH_VARIABLE_OWNER);
        addParameterRequest(pathBuilder, REQUEST_PARAM_STATE);

        return get(pathBuilder.toString(), userId, parameters);
    }
}
