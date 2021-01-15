package com.example.course_project.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Getter
@RequiredArgsConstructor
public class LoggedInUser {
    private final String userId;
    private final String displayName;
}