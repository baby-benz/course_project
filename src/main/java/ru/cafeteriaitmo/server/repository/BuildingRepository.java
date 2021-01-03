package ru.cafeteriaitmo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cafeteriaitmo.server.domain.entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}
