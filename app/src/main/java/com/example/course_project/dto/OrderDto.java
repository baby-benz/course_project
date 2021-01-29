package com.example.course_project.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@RequiredArgsConstructor
public class OrderDto implements Serializable {
    private static final long serialVersionUID = 8940694154955849897L;
    private Long id;
    private LocalDateTime orderedOn;
    private String monitorCode;
    private String status;

    private ArrayList<Long> productIds;
    private String buildingName;
    private String personalNumber;
}
