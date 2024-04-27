package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Map;

public interface MPAStorage {

    public Map<String, Object> getMPA(Integer id) throws NotFoundException;

    public List<Map<String, Object>> getAllMPA();

}
