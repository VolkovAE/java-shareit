package ru.practicum.shareit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static <T> long getNextId(Map<Long, T> map) {
        long currentMaxId = map.keySet().stream() // открыл поток Stream<Long>
                .mapToLong(id -> id)    // преобразовал поток Stream<Long> (поток объектов Long)
                // в поток значений примитивного типа long StreamLong, чтобы воспользоваться max
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
