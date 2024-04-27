package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.response.UserFilmResponse;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.dao.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @PostMapping // добавление фильма
    public Film addFilm(@Valid @RequestBody Film filmFromRequest) throws NotFoundException, BadRequestException {
        log.info(String.format("Request body: %s", filmFromRequest.toString()));
        Film film = filmStorage.addFilm(filmFromRequest);
        log.info(String.format("Response body: %s", film.toString()));
        return film;
    }

    @PutMapping // обновление фильма
    public Film updateFilm(@Valid @RequestBody Film film) throws NotFoundException, BadRequestException {
        log.info(String.format("Request body: %s", film.toString()));
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info(String.format("Response body: %s", film.toString()));
        return updatedFilm;
    }

    @GetMapping // получение всех фильмов
    public List<Film> getAllFilms() {
        List<Film> films =  filmStorage.getAllFilms();
        log.info(String.format("Response body: %s", films.toString()));
        return films;
    }

    @GetMapping("/{id}") // получение фильма
    public Film getFilm(@PathVariable("id") Integer film_id) throws NotFoundException {
        log.info(String.format("Request body: film_id = %d.", film_id));
        Film film = filmStorage.getFilm(film_id);
        log.info(String.format("Response body: %s", film.toString()));
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public UserFilmResponse setLike(@PathVariable("id") Integer filmId,
                                    @PathVariable Integer userId) throws NotFoundException {
        log.info(String.format("Request body: film_id = %d, user_id = %d", filmId, userId));
        UserFilmResponse userFilmResponse = filmService.setLike(userId, filmId);
        log.info(String.format("Response body: %s", userFilmResponse.toString()));
        return userFilmResponse;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public UserFilmResponse removeLike(@PathVariable("id") Integer filmId,
                                       @PathVariable Integer userId) throws NotFoundException {
        log.info(String.format("Request body: film_id = %d, user_id = %d", filmId, userId));
        UserFilmResponse userFilmResponse = filmService.removeLike(userId, filmId);
        log.info(String.format("Response body: %s", userFilmResponse.toString()));
        return userFilmResponse;
    }

    @GetMapping("/popular")
    public List<Film> getRatedFilms(@RequestParam(required = false) Integer count) {
        log.info(String.format("Request body: %s", count));
        List<Film> films = filmService.getRatedFilms(count);
        log.info(String.format("Request body: %s", films));
        return films;
    }

}
