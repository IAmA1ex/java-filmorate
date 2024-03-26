package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.response.UserFilmResponse;

import java.util.List;

public interface FilmStorage {

    public Film addFilm(Film filmFromRequest);

    public Film updateFilm(Film film) throws NotFoundException;

    public List<Film> getAllFilms();

    public Film getFilm(int id) throws NotFoundException;
}
