package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getGenre(Integer id) throws NotFoundException {
        try {
            String sql = "SELECT genre " +
                    "FROM genre " +
                    "WHERE genre_id = ?; ";
            String genre = jdbcTemplate.queryForObject(sql, String.class, id);
            return Map.of("id", id, "name", Objects.requireNonNull(genre));
        } catch (Exception e) {
            throw new NotFoundException(String.format("Genre with id=%d has not found.", id));
        }
    }

    public List<Map<String, Object>> getAllGenres() {
        String sql = "SELECT * " +
                "FROM genre; ";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

        Set<Map<String, Object>> genres = new HashSet<>();
        while (sqlRowSet.next()) {
            genres.add(Map.of("id", sqlRowSet.getInt("GENRE_ID"),
                    "name", Objects.requireNonNull(sqlRowSet.getString("GENRE"))));
        }

        return genres.stream()
                .sorted(Comparator.comparing(map -> (int) map.get("id")))
                .collect(Collectors.toList());
    }
}
