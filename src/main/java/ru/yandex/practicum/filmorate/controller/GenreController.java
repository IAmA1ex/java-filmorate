package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private static final Logger log = LoggerFactory.getLogger(GenreController.class);
    private final GenreStorage genreStorage;

    @GetMapping("/{id}") // получение фильма
    public Map<String, Object> getGenre(@PathVariable Integer id) throws NotFoundException {
        log.info(String.format("Request body: genre_id = %d", id));
        Map<String, Object> map = genreStorage.getGenre(id);
        log.info(String.format("Request body: %s", map));
        return map;
    }

    @GetMapping // получение всех фильмов
    public List<Map<String, Object>> getAllGenres() {
        List<Map<String, Object>> mapList = genreStorage.getAllGenres();
        log.info(String.format("Request body: %s", mapList));
        return mapList;
    }

}
