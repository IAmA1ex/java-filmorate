package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.SameObjectsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> makeFriends(Integer userId1, Integer userId2) throws NotFoundException, SameObjectsException {
        if (!userId1.equals(userId2)) {
            if (userStorage.dbContainsUser(userId1) && userStorage.dbContainsUser(userId2)) {
                Map<Integer, Integer> friendsStatus = userStorage.getFriendsStatus(userId1, userId2);
                if (!friendsStatus.isEmpty()) {
                    userStorage.updateFriendStatus(userId1, userId2, 1);
                } else {
                    userStorage.addFriendStatus(userId1, userId2, 1);
                    userStorage.addFriendStatus(userId2, userId1, 2);
                }
                return List.of(userStorage.getUser(userId1), userStorage.getUser(userId2));
            }
            throw new NotFoundException("User not found.");
        }
        throw new SameObjectsException("Identical IDs. The user cannot add himself as a friend.");
    }

    public List<User> removeFriends(Integer userId1, Integer userId2) throws NotFoundException, SameObjectsException {
        if (!userId1.equals(userId2)) {
            if (userStorage.dbContainsUser(userId1) && userStorage.dbContainsUser(userId2)) {
                Map<Integer, Integer> friendsStatus = userStorage.getFriendsStatus(userId1, userId2);
                if (!friendsStatus.isEmpty()) {
                    userStorage.updateFriendStatus(userId1, userId2, 2);
                }
                return List.of(userStorage.getUser(userId1), userStorage.getUser(userId2));
            }
            throw new NotFoundException("User not found.");
        }
        throw new SameObjectsException("Identical IDs. The user cannot remove himself as a friend.");
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) throws NotFoundException, SameObjectsException {
        if (!user1Id.equals(user2Id)) {
            User u1 = userStorage.getUser(user1Id);
            User u2 = userStorage.getUser(user2Id);
            Set<Integer> common = new HashSet<>(u1.getFriends());
            common.retainAll(u2.getFriends());
            List<User> commonFriends = new ArrayList<>();
            for (Integer id : common) {
                commonFriends.add(userStorage.getUser(id));
            }
            return commonFriends;
        }
        throw new SameObjectsException("Identical IDs.");
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
