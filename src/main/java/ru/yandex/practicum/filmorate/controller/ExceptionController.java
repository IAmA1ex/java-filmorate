package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.SameObjectsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(final ValidationException e) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", e.getMessage());
        log.info("ValidationException. Response: " + response);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(final MethodArgumentNotValidException e) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("errors", e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()));
        response.put(e.getObjectName(), e.getBindingResult().getTarget());
        log.info("MethodArgumentNotValidException. Response: " + response);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(final SameObjectsException e) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", e.getMessage());
        log.info("SameObjectsException. Response: " + response);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(final BadRequestException e) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", e.getMessage());
        log.info("BadRequestException. Response: " + response);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> notFound(final NotFoundException e) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", e.getMessage());
        log.info("NotFoundException. Response: " + response);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> internalServerError(final Exception e) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", e.getMessage());
        log.info("Exception. Response: " + response);
        return response;
    }
}
