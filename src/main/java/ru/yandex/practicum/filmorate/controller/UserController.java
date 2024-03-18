package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    public int counterId = 1;
    public final HashMap<Integer, User> users = new HashMap<>();

    @PostMapping // создание пользователя
    public User addUser(@Valid @RequestBody User userFromRequest) {
        User user = userFromRequest.toBuilder()
                .name(userFromRequest.getName() == null || userFromRequest.getName().isEmpty() ?
                        userFromRequest.getLogin() : userFromRequest.getName())
                .id(counterId++)
                .build();
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping // обновление пользователя
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (user.getName().isEmpty()) user.setName(user.getLogin());
            users.replace(user.getId(), user);
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film with such id was not found.");
        }
    }

    @GetMapping // получение списка всех пользователей
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

}
