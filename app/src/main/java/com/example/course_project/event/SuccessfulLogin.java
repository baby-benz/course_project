package com.example.course_project.event;

import com.example.course_project.data.model.LoggedInUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SuccessfulLogin {
    private LoggedInUser loggedInUser;
}
