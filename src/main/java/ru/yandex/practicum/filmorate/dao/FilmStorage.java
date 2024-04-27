package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public Film addFilm(Film filmFromRequest) throws NotFoundException, BadRequestException;

    public Film updateFilm(Film filmFromRequest) throws NotFoundException, BadRequestException;

    public List<Film> getAllFilms();

    public Film getFilm(Integer id) throws NotFoundException;

    public List<Film> getRatedFilms(Integer limit);
}
