package ru.cafeteriaitmo.server.controller.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Empty Request Body.")
public class EmptyRequestBody extends Exception {
    private static final long serialVersionUID = 7378034598454614312L;
    private final String entity;

    /**
     * Конструктор ошибки пустого тела запроса
     * @param entity тип сущности
     * @return ошибка по сущности и номеру
     */
    public static EmptyRequestBody createWith(String entity) {
        return new EmptyRequestBody(entity);
    }

    public String getMessage() {
        return "The server reported: An empty request body was received! Cannot create " + entity;
    }
}
