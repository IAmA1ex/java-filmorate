package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    public int counterId = 1;
    public final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User userFromRequest) {
        User user = userFromRequest.toBuilder()
                .name(userFromRequest.getName() == null || userFromRequest.getName().isBlank() ?
                        userFromRequest.getLogin() : userFromRequest.getName())
                .id(counterId++)
                .build();
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        if (users.containsKey(user.getId())) {
            if (user.getName().isBlank()) user.setName(user.getLogin());
            users.replace(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException(String.format("User with id=%d was not found.", user.getId()));
        }
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public User getUser(int id) throws NotFoundException {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException(String.format("User with id=%d was not found.", id));
        }
    }

}
