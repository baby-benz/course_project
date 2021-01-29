package ru.cafeteriaitmo.server.service;

import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.User;

public interface UserService {
    User addUser(User user);
    User getUser(Long id) throws NoEntityException;
    User getUserByPersonalNumber(String personalNumber) throws NoEntityException;
}
