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
        Film film = filmFromRequest.toBuilder()
                .id(counterId++)
                .build();
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping // обновление фильма
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            return film;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film with such id was not found.");
        }
    }

    @GetMapping // получение всех фильмов
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

}
