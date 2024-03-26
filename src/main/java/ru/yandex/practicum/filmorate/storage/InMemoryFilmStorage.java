package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    public int counterId = 1;
    public final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film filmFromRequest) {
        Film film = filmFromRequest.toBuilder()
                .id(counterId++)
                .build();
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException(String.format("Film with id=%d was not found.", film.getId()));
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public Film getFilm(int id) throws NotFoundException {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException(String.format("Film with id=%d was not found.", id));
        }
    }
}
