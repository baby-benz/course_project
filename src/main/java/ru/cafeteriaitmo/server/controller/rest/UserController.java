package ru.cafeteriaitmo.server.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cafeteriaitmo.server.domain.entity.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping
    public @ResponseBody User getUser(@RequestParam String surname, @RequestParam String name) {
        return null;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerUser(User user) {

    }
}
