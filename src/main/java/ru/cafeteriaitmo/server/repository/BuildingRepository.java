package ru.cafeteriaitmo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cafeteriaitmo.server.domain.entity.Building;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
}
