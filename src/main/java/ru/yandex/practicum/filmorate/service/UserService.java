package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void makeFriends(User u1, User u2) {
        u1.getFriends().add(u2.getId());
        u2.getFriends().add(u1.getId());
    }

    public void removeFriends(User u1, User u2) {
        u1.getFriends().remove(u2.getId());
        u2.getFriends().remove(u1.getId());
    }

    public List<User> getCommonFriends(User u1, User u2) throws NotFoundException {
        Set<Integer> common = new HashSet<>(u1.getFriends());
        common.retainAll(u2.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (Integer id: common) {
            commonFriends.add(userStorage.getUser(id));
        }
        return commonFriends;
    }
}
