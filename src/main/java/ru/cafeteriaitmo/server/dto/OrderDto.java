package ru.cafeteriaitmo.server.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class OrderDto implements Serializable {
    private static final long serialVersionUID = 775867362691901442L;
    private Long id;
    private String orderedOn;
    private String monitorCode;
    private String status;

    private ArrayList<Long> productIds;
    private String buildingName;
    private String userPersonalNumber;
}
