package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    public int counterId = 1;
    public final HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping // добавление фильма
    public Film addFilm(@Valid @RequestBody Film filmFromRequest) {
        log.info("Request body: " + filmFromRequest.toString());
        Film film = filmFromRequest.toBuilder()
                .id(counterId++)
                .build();
        films.put(film.getId(), film);
        log.info("Response body: " + film.toString());
        return film;
    }

    @PutMapping // обновление фильма
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Request body: " + film.toString());
            films.replace(film.getId(), film);
            log.info("Request body: " + film.toString());
            return film;
        } else {
            String message = "Film with such id was not found.";
            log.info("Request body: " + film.toString());
            log.info(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
    }

    @GetMapping // получение всех фильмов
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

}
