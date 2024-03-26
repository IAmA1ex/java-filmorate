package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    public User addUser(User userFromRequest);

    public User updateUser(User user) throws NotFoundException;

    public List<User> getAllUsers();

    public User getUser(int id) throws NotFoundException;

}
