package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> makeFriends(Integer user1Id, Integer user2Id) throws NotFoundException {
        User u1 = userStorage.getUser(user1Id);
        User u2 = userStorage.getUser(user2Id);
        u1.getFriends().add(u2.getId());
        u2.getFriends().add(u1.getId());
        return List.of(u1, u2);
    }

    public List<User> removeFriends(Integer user1Id, Integer user2Id) throws NotFoundException {
        User u1 = userStorage.getUser(user1Id);
        User u2 = userStorage.getUser(user2Id);
        u1.getFriends().remove(u2.getId());
        u2.getFriends().remove(u1.getId());
        return List.of(u1, u2);
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) throws NotFoundException {
        User u1 = userStorage.getUser(user1Id);
        User u2 = userStorage.getUser(user2Id);
        Set<Integer> common = new HashSet<>(u1.getFriends());
        common.retainAll(u2.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (Integer id: common) {
            commonFriends.add(userStorage.getUser(id));
        }
        return commonFriends;
    }

    public List<User> getFriends(Integer userId) throws NotFoundException {
        User user = userStorage.getUser(userId);
        return user.getFriends().stream()
                .map(id -> {
                    try {
                        return userStorage.getUser(id);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
