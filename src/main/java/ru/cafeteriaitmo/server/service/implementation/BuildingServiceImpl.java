package ru.cafeteriaitmo.server.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cafeteriaitmo.server.domain.entity.Building;
import ru.cafeteriaitmo.server.repository.BuildingRepository;
import ru.cafeteriaitmo.server.service.BuildingService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;

    public List<Building> getAll() {
        return buildingRepository.findAll();
    }
}
