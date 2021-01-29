package ru.cafeteriaitmo.server.controller.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity not found.")
public class NoEntityException extends Exception {
    private static final long serialVersionUID = -5774500457280976647L;
    private final String entity;
    private final String uid;

    /**
     * Конструктор ошибки отсутсвия сущности
     * @param entity тип сущности
     * @param uid запрашиваемый уникальный номер сущности
     * @return ошибка по сущности и номеру
     */
    public static NoEntityException createWith(String entity, String uid) {
        return new NoEntityException(entity, uid);
    }

    public String getMessage() {
            return "The server reported: " + entity + " with ID=" + uid + " was not found.";
    }
}
