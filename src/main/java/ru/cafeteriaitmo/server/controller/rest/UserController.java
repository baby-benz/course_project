package ru.cafeteriaitmo.server.controller.rest;

import org.springframework.web.bind.annotation.*;
import ru.cafeteriaitmo.server.domain.entity.User;

@RestController("/api/user")
public class UserController {

    @GetMapping
    public @ResponseBody User getUser(@RequestParam String surname, @RequestParam String name) {
        return null;
    }

    @PostMapping
    public void registerUser(User user) {

    }
}
