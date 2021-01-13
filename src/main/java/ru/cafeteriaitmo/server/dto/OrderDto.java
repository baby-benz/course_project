package ru.cafeteriaitmo.server.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderDto implements Serializable {
    private static final long serialVersionUID = 775867362691901442L;
    private LocalDateTime orderedOn;
    private String monitorCode;
    private String status;
}
