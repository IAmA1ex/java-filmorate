package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserStorage {

    public boolean dbContainsUser(Integer userId);

    public User addUser(User userFromRequest) throws Exception;

    public User updateUser(User user) throws Exception;

    public List<User> getAllUsers();

    public User getUser(Integer id) throws NotFoundException;

    public List<User> getUsers(Set<Integer> id);

    public Set<Integer> getFriends(Integer userId);

    public Map<Integer, Integer> getFriendsStatus(Integer userId1, Integer userId2);

    public void updateFriendStatus(Integer userId1, Integer userId2, Integer statusId);

    public void addFriendStatus(Integer userId1, Integer userId2, Integer statusId);

    public Set<Integer> getLikedFilms(Integer userId);

    public void setLike(Integer userId, Integer filmId) throws Exception;

    public void removeLike(Integer userId, Integer filmId);

}
