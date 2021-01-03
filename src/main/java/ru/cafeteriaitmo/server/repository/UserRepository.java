package ru.cafeteriaitmo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cafeteriaitmo.server.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
