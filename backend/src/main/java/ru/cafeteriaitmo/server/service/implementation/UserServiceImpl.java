package ru.cafeteriaitmo.server.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.User;
import ru.cafeteriaitmo.server.repository.UserRepository;
import ru.cafeteriaitmo.server.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(Long id) throws NoEntityException {
        return userRepository.findById(id).orElseThrow(() ->
                new NoEntityException(User.class.getSimpleName().toLowerCase(), id));
    }
}
