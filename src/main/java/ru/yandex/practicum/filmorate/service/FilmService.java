package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.response.UserFilmResponse;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public UserFilmResponse setLike(Integer userId, Integer filmId) throws NotFoundException {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        if (!user.getLikedFilms().contains(film.getId())) {
            user.getLikedFilms().add(film.getId());
            film.setLikes(film.getLikes() + 1);
        }
        return new UserFilmResponse(user, film);
    }

    public UserFilmResponse removeLike(Integer userId, Integer filmId) throws NotFoundException {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        if (user.getLikedFilms().contains(film.getId())) {
            user.getLikedFilms().remove(film.getId());
            film.setLikes(film.getLikes() - 1);
        }
        return new UserFilmResponse(user, film);
    }

    public List<Film> getRatedFilms(Integer count) {
        if (count == null) count = 10;
        return filmStorage.getAllFilms().stream()
                .sorted(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (o1.getLikes() == o2.getLikes()) return Integer.compare(o1.getId(), o2.getId());
                        return Integer.compare(o2.getLikes(), o1.getLikes());
                    }
                })
                .limit(count)
                .collect(Collectors.toList());
    }

}
