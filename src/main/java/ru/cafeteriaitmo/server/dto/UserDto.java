package ru.cafeteriaitmo.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 3040401765434495059L;
    private String monitorCode;
    private String name;
    private String surname;
}
