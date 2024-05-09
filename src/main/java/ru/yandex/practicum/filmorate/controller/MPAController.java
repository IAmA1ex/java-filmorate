package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.MPAStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MPAController {

    private static final Logger log = LoggerFactory.getLogger(MPAController.class);
    private final MPAStorage mpaStorage;

    @GetMapping("/{id}") // получение фильма
    public Map<String, Object> getMPA(@PathVariable Integer id) throws NotFoundException {
        log.info(String.format("Request body: mpa_id = %d", id));
        Map<String, Object> map = mpaStorage.getMPA(id);
        log.info(String.format("Request body: %s", map));
        return map;
    }

    @GetMapping // получение всех фильмов
    public List<Map<String, Object>> getAllMPA() {
        List<Map<String, Object>> mapList = mpaStorage.getAllMPA();
        log.info(String.format("Request body: %s", mapList));
        return mapList;
    }

}
