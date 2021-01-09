package ru.cafeteriaitmo.server.service;

import ru.cafeteriaitmo.server.controller.exception.NoEntityException;
import ru.cafeteriaitmo.server.domain.entity.Building;

import java.util.List;

public interface BuildingService {
    List<Building> getAll();
    Building getBuildingByName(String buildingName) throws NoEntityException;
}
