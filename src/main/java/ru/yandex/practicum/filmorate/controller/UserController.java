package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.SameObjectsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final UserService userService;

    @PostMapping // создание пользователя
    public User addUser(@Valid @RequestBody User userFromRequest) {
        log.info("Request body: " + userFromRequest.toString());
        User user = userStorage.addUser(userFromRequest);
        log.info("Response body: " + user.toString());
        return user;
    }

    @PutMapping // обновление пользователя
    public User updateUser(@Valid @RequestBody User user) throws NotFoundException {
        log.info("Request body: " + user.toString());
        User updatedUser = userStorage.updateUser(user);
        log.info("Response body: " + updatedUser.toString());
        return updatedUser;
    }

    @GetMapping // получение списка всех пользователей
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<User> makeFriends(@PathVariable("id") Integer user1Id,
                                  @PathVariable("friendId") Integer user2Id) throws NotFoundException, SameObjectsException {
        List<User> friends = userService.makeFriends(user1Id, user2Id);
        log.info(Map.of("id", user1Id, "friendId", user2Id, "user1",
                friends.get(0), "user2", friends.get(1)).toString());
        return friends;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> removeFriends(@PathVariable("id") Integer user1Id,
                                    @PathVariable("friendId") Integer user2Id) throws NotFoundException {
        List<User> friends = userService.removeFriends(user1Id, user2Id);
        log.info(Map.of("id", user1Id, "friendId", user2Id, "user1",
                friends.get(0), "user2", friends.get(1)).toString());
        return friends;
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") Integer userId) throws NotFoundException {
        User user = userStorage.getUser(userId);
        List<User> friends = userService.getFriends(userId);
        log.info(Map.of("user", user, "friends", friends).toString());
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer user1Id,
                                       @PathVariable("otherId") Integer user2Id) throws NotFoundException, SameObjectsException {
        List<User> commonFriends = userService.getCommonFriends(user1Id, user2Id);
        log.info(Map.of("id", user1Id,
                "otherId", user2Id,
                "user1", userStorage.getUser(user1Id),
                "user2", userStorage.getUser(user2Id),
                "common friends", commonFriends).toString());
        return commonFriends;
    }



}
