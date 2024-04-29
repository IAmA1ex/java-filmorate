package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.response.UserFilmResponse;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public UserFilmResponse setLike(Integer userId, Integer filmId) throws Exception {
        userStorage.setLike(userId, filmId);
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        return new UserFilmResponse(user, film);
    }

    public UserFilmResponse removeLike(Integer userId, Integer filmId) throws NotFoundException {
        userStorage.removeLike(userId, filmId);
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        return new UserFilmResponse(user, film);
    }

    public List<Film> getRatedFilms(Integer count) {
        if (count == null || count <= 0) count = 10;
        return filmStorage.getRatedFilms(count);
    }

}
