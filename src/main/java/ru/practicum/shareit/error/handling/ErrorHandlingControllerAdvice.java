package ru.practicum.shareit.error.handling;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandlingControllerAdvice.class);

    /**
     * Отлавливаю исключения типа ConstraintViolationException, ошибка в параметрах запроса, параметрах пути.
     *
     * @param e - исключение типа ConstraintViolationException
     * @return - возвращаем объект класса ValidationErrorResponse со списком нарушений
     */

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());

        log.error("Ошибка валидации: {}", violations);

        return new ValidationErrorResponse(violations);
    }

    /**
     * Отлавливаю исключения типа MethodArgumentNotValidException, объект не прошел валидацию.
     *
     * @param e - исключение типа ConstraintViolationException
     * @return - возвращаем объект класса ValidationErrorResponse со списком нарушений
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        log.error("Объект не прошел валидацию: {}", violations);

        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onValidationException(ValidationException e) {
        Violation violation = new Violation("-", e.getMessage());
        List<Violation> violationList = List.of(violation);

        return new ValidationErrorResponse(violationList);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onNotFoundException(NotFoundException e) {
        Violation violation = new Violation("-", e.getMessage());
        List<Violation> violationList = List.of(violation);

        return new ValidationErrorResponse(violationList);
    }

    @ExceptionHandler(DuplicatedDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ValidationErrorResponse onDuplicatedDataException(DuplicatedDataException e) {
        Violation violation = new Violation("-", e.getMessage());
        List<Violation> violationList = List.of(violation);

        return new ValidationErrorResponse(violationList);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse runtimeException(RuntimeException e) {
        return new ErrorResponse("Произошла непредвиденная ошибка. Информация: " + e.getMessage() + ".");
    }
}
