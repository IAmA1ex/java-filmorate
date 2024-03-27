package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.response.UserFilmResponse;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmService filmService;

    @PostMapping // добавление фильма
    public Film addFilm(@Valid @RequestBody Film filmFromRequest) {
        log.info("Request body: " + filmFromRequest.toString());
        Film film = filmStorage.addFilm(filmFromRequest);
        log.info("Response body: " + film.toString());
        return film;
    }

    @PutMapping // обновление фильма
    public Film updateFilm(@Valid @RequestBody Film film) throws NotFoundException {
        log.info("Request body: " + film.toString());
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Request body: " + film.toString());
        return updatedFilm;
    }

    @GetMapping // получение всех фильмов
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/{id}") // получение фильма
    public Film getFilm(@PathVariable Integer id) throws NotFoundException {
        return filmStorage.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public UserFilmResponse setLike(@PathVariable("id") Integer filmId,
                                    @PathVariable Integer userId) throws NotFoundException {
        return filmService.setLike(userId, filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public UserFilmResponse removeLike(@PathVariable("id") Integer filmId,
                                       @PathVariable Integer userId) throws NotFoundException {
        return filmService.removeLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getRatedFilms(@RequestParam(required = false) Integer count) {
        return filmService.getRatedFilms(count);
    }

}
