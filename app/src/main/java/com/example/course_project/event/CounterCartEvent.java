package com.example.course_project.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CounterCartEvent {
    private boolean success;
}
