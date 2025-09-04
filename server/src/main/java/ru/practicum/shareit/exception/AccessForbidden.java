package ru.practicum.shareit.exception;

import org.slf4j.Logger;

public class AccessForbidden extends RuntimeException {
    public AccessForbidden(String message) {
        super(message);
    }

    public AccessForbidden(String message, Logger logger) {
        this(message);
        logger.error(message);
    }
}
