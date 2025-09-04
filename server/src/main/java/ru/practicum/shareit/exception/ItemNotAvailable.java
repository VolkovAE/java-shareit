package ru.practicum.shareit.exception;

import org.slf4j.Logger;

public class ItemNotAvailable extends RuntimeException {
    public ItemNotAvailable(String message) {
        super(message);
    }

    public ItemNotAvailable(String message, Logger logger) {
        this(message);
        logger.error(message);
    }
}
