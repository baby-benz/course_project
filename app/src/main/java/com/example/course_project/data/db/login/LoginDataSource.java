package com.example.course_project.data.db.login;

import com.example.course_project.data.model.Common;
import com.example.course_project.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result login(String userId, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            userId,
                            "Jane Doe (" + userId + ")");
            Common.LOGGED_IN_USER = fakeUser;
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
        Common.LOGGED_IN_USER = null;
    }
}