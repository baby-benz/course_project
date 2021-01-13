package ru.cafeteriaitmo.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BuildingDto implements Serializable {
    private String name;
    private String address; //TODO: думаю адрес не надо передавать/хранить
}
