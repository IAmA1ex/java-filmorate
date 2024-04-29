package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.SameObjectsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserStorage userStorage;
    private final UserService userService;

    @PostMapping // создание пользователя
    public User addUser(@Valid @RequestBody User userFromRequest) throws Exception {
        log.info(String.format("Request body: %s", userFromRequest.toString()));
        User user = userStorage.addUser(userFromRequest);
        log.info(String.format("Response body: %s", user.toString()));
        return user;
    }

    @PutMapping // обновление пользователя
    public User updateUser(@Valid @RequestBody User user) throws Exception {
        log.info(String.format("Request body: %s", user.toString()));
        userStorage.updateUser(user);
        User updatedUser = userStorage.getUser(user.getId());
        log.info(String.format("Response body: %s", updatedUser.toString()));
        return updatedUser;
    }

    @GetMapping // получение списка всех пользователей
    public List<User> getAllUsers() {
        List<User> list = userStorage.getAllUsers();
        log.info(String.format("Response body: count of users = %d.", list.size()));
        return list;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<User> makeFriends(@PathVariable("id") Integer user1Id,
                                  @PathVariable("friendId") Integer user2Id) throws NotFoundException, SameObjectsException {
        log.info(String.format("Request body: user1Id = %d, user2Id = %d.", user1Id, user2Id));
        List<User> friends = userService.makeFriends(user1Id, user2Id);
        log.info(Map.of("id", user1Id, "friendId", user2Id, "user1",
                friends.get(0), "user2", friends.get(1)).toString());
        return friends;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> removeFriends(@PathVariable("id") Integer user1Id,
                                    @PathVariable("friendId") Integer user2Id) throws NotFoundException, SameObjectsException {
        log.info(String.format("Request body: user1Id = %d, user2Id = %d.", user1Id, user2Id));
        List<User> friends = userService.removeFriends(user1Id, user2Id);
        log.info(String.format("Response body: %s", Map.of("id", user1Id, "friendId", user2Id, "user1",
                friends.get(0), "user2", friends.get(1)).toString()));
        return friends;
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") Integer userId) throws NotFoundException {
        log.info(String.format("Request body: userId = %d.", userId));
        List<User> friends = userService.getFriends(userId);
        log.info(String.format("Response body: %s", Map.of("user_id", userId, "friends", friends).toString()));
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer user1Id,
                                       @PathVariable("otherId") Integer user2Id) throws NotFoundException, SameObjectsException {
        log.info(String.format("Request body: user1Id = %d, user2Id = %d.", user1Id, user2Id));
        List<User> commonFriends = userService.getCommonFriends(user1Id, user2Id);
        log.info(String.format("Response body: %s", Map.of("id", user1Id,
                "otherId", user2Id,
                "user1", userStorage.getUser(user1Id),
                "user2", userStorage.getUser(user2Id),
                "common friends", commonFriends).toString()));
        return commonFriends;
    }

}
