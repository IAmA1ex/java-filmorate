package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public User addUser(User userFromRequest);

    public User updateUser(User user) throws NotFoundException;

    public List<User> getAllUsers();

    public User getUser(int id) throws NotFoundException;

}
