package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void setLike(User user, Film film) {
        if (!user.getLikedFilms().contains(film.getId())) {
            user.getLikedFilms().add(film.getId());
            film.setLikes(film.getLikes() + 1);
        }
    }

    public void removeLike(User user, Film film) {
        if (user.getLikedFilms().contains(film.getId())) {
            user.getLikedFilms().remove(film.getId());
            film.setLikes(film.getLikes() - 1);
        }
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
