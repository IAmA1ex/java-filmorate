package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FilmDbStorage implements FilmStorage {

    private static final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film addFilm(Film filmFromRequest) throws NotFoundException, BadRequestException {

        try {
            Integer filmIdInDb;
            Integer ratingMpaId = (Integer) filmFromRequest.getMpa().getOrDefault("id", null);
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO films (film_name, description, release_date, duration, rating_mpa_id) " +
                                "VALUES (?, ?, ?, ?, ?); ",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, filmFromRequest.getName());
                ps.setString(2, filmFromRequest.getDescription());
                ps.setString(3, dateFormat.format(filmFromRequest.getReleaseDate()));
                ps.setInt(4, filmFromRequest.getDuration());
                ps.setInt(5, ratingMpaId);
                return ps;
            }, keyHolder);
            filmIdInDb = keyHolder.getKeyAs(Integer.class);

            List<Integer> genresId;
            try {
                genresId = filmFromRequest.getGenres().stream()
                        .filter(m -> m != null && m.containsKey("id")
                                && m.get("id") != null && m.get("id") instanceof Integer)
                        .map(m -> (Integer) m.get("id"))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                genresId = Collections.emptyList();
            }

            for (Integer i : genresId) {
                String sql = "INSERT INTO film_genre (film_id, genre_id) " +
                        "VALUES (?, ?); ";
                jdbcTemplate.update(sql, filmIdInDb, i);
            }

            return getFilm(filmIdInDb);
        } catch (Exception e) {
            if (e.getMessage().contains("FOREIGN KEY(RATING_MPA_ID)")) {
                throw new BadRequestException("MPA id is not found.");
            } else if (e.getMessage().contains("FOREIGN KEY(GENRE_ID)")) {
                throw new BadRequestException("Genre id is not found.");
            }
            throw e;
        }
    }

    public Film updateFilm(Film filmFromRequest) throws NotFoundException, BadRequestException {

        try {
            Integer ratingMpaId = (Integer) filmFromRequest.getMpa().getOrDefault("id", null);

            String sql = "UPDATE films " +
                    "SET film_name = ?, description = ?, release_date = ?, duration = ?, rating_mpa_id = ? " +
                    "WHERE film_id = ?; ";
            jdbcTemplate.update(sql, filmFromRequest.getName(),
                    filmFromRequest.getDescription(),
                    dateFormat.format(filmFromRequest.getReleaseDate()),
                    filmFromRequest.getDuration(),
                    ratingMpaId,
                    filmFromRequest.getId());

            sql = "DELETE FROM film_genre WHERE film_id = ?; ";
            jdbcTemplate.update(sql, filmFromRequest.getId());

            if (filmFromRequest.getGenres() != null) {
                List<Integer> genresId = filmFromRequest.getGenres().stream()
                        .map(m -> (Integer) m.get("id"))
                        .collect(Collectors.toList());
                for (Integer i : genresId) {
                    sql = "INSERT INTO film_genre (film_id, genre_id) " +
                            "VALUES (?, ?); ";
                    jdbcTemplate.update(sql, filmFromRequest.getId(), i);
                }
            }

            return getFilm(filmFromRequest.getId());
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            if (e.getMessage().contains("FOREIGN KEY(RATING_MPA_ID)")) {
                throw new BadRequestException("MPA id is not found.");
            } else if (e.getMessage().contains("FOREIGN KEY(GENRE_ID)")) {
                throw new BadRequestException("Genre id is not found.");
            }
            throw e;
        }
    }

    public List<Film> getAllFilms() {
        String sql = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_mpa_id = rm.rating_mpa_id; ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    public Film getFilm(Integer filmId) throws NotFoundException {

        String sql = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS r ON f.rating_mpa_id = r.rating_mpa_id " +
                "WHERE f.film_id = ?; ";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, filmId);

        List<Map<String, Object>> genres = getGenres(filmId);
        Integer likes = getLikes(filmId);

        if(filmRows.next()) {

            Map<String, Object> mpa = Map.of("id", filmRows.getInt("rating_mpa_id"),
                    "name", Objects.requireNonNull(filmRows.getString("rating_mpa")));


            Film film = Film.builder()
                    .id(filmId)
                    .name(filmRows.getString("film_name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(LocalDate.parse(Objects.requireNonNull(filmRows.getString("release_date")), dateFormat))
                    .duration(filmRows.getInt("duration"))
                    .likes(likes)
                    .genres(genres)
                    .mpa(mpa)
                    .build();

            log.info("Найден фильм {}", film.getId());

            return film;
        } else {
            throw new NotFoundException(String.format("Фильм с идентификатором %d не найден.", filmId));
        }
    }

    public Integer getLikes(Integer filmId) {
        String sql =
                "SELECT COUNT(user_id) AS count " +
                        "FROM liked_films " +
                        "WHERE film_id = ?; ";
        return jdbcTemplate.queryForObject(sql, Integer.class, filmId);
    }

    public List<Map<String, Object>> getGenres(Integer filmId) {
        String sql = "SELECT g.genre_id, g.genre " +
                "FROM film_genre AS fg " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?; ";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, filmId);
        Set<Map<String, Object>> genres = new HashSet<>();
        while (sqlRowSet.next()) {
            genres.add(Map.of("id", sqlRowSet.getInt("GENRE_ID"),
                    "name", Objects.requireNonNull(sqlRowSet.getString("GENRE"))));
        }
        return genres.stream()
                .map(map -> {
                    Map<String, Object> genre = new LinkedHashMap<>();
                    genre.put("id", map.get("id"));
                    genre.put("name", map.get("name"));
                    return genre;
                })
                .sorted(Comparator.comparingInt(m -> (int) m.get("id")))
                .collect(Collectors.toList());
    }

    public List<Film> getRatedFilms(Integer limit) {
        String sql = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN ( " +
                "   SELECT film_id, COUNT(user_id) AS likes_count " +
                "   FROM liked_films" +
                "   GROUP BY film_id ) AS fc ON f.film_id = fc.film_id " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_mpa_id = rm.rating_mpa_id " +
                "ORDER BY fc.likes_count DESC " +
                "LIMIT ?; ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), limit);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {

        Integer id = rs.getInt("film_id");
        String name = rs.getString("film_name");
        String description = rs.getString("description");
        LocalDate releaseDate = LocalDate.parse(rs.getString("release_date"), dateFormat);
        int duration = rs.getInt("duration");

        Map<String, Object> mpa = Map.of("id", rs.getInt("rating_mpa_id"),
                "name", rs.getString("rating_mpa"));

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .likes(getLikes(id))
                .genres(getGenres(id))
                .mpa(mpa)
                .build();
    }

}
