package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MPAStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MPADbStorage implements MPAStorage {

    private static final Logger log = LoggerFactory.getLogger(MPADbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, Object> getMPA(Integer id) throws NotFoundException {
        try {
            String sql = "SELECT * " +
                    "FROM rating_mpa " +
                    "WHERE rating_mpa_id = ?; ";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, id);
            return Map.of("id", map.get("RATING_MPA_ID"), "name", map.get("RATING_MPA"));
        } catch (Exception e) {
            throw new NotFoundException(String.format("MPA with id=%d has not found.", id));
        }

    }

    @Override
    public List<Map<String, Object>> getAllMPA() {
        String sql = "SELECT * " +
                "FROM rating_mpa; ";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

        Set<Map<String, Object>> mpaSet = new HashSet<>();
        while (sqlRowSet.next()) {
            mpaSet.add(Map.of("id",sqlRowSet.getInt("RATING_MPA_ID"),
                    "name", Objects.requireNonNull(sqlRowSet.getString("RATING_MPA"))));
        }

        return mpaSet.stream()
                .sorted(Comparator.comparing(map -> (int) map.get("id")))
                .collect(Collectors.toList());
    }
}
