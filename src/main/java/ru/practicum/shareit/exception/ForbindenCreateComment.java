package ru.practicum.shareit.exception;

import org.slf4j.Logger;

public class ForbindenCreateComment extends RuntimeException {
    public ForbindenCreateComment(String message) {
        super(message);
    }

    public ForbindenCreateComment(String message, Logger logger) {
        this(message);
        logger.error(message);
    }
}
