package ru.practicum.shareit.exception;

import org.slf4j.Logger;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Logger logger) {
        this(message);
        logger.error(message);
    }
}
