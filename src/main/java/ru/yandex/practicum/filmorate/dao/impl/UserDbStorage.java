package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User addUser(User userFromRequest) throws NotFoundException {
        String sql = "INSERT INTO users (email, login, username, birthday) " +
                "VALUES (?, ?, ?, ?); ";

        jdbcTemplate.update(sql, userFromRequest.getEmail(),
                userFromRequest.getLogin(),
                userFromRequest.getName(),
                dateFormat.format(userFromRequest.getBirthday()));

        sql = "SELECT user_id " +
                "FROM users " +
                "WHERE login = ?; ";

        Integer id = jdbcTemplate.queryForObject(sql, new Object[]{userFromRequest.getLogin()}, Integer.class);

        return getUser(id);
    }

    public User updateUser(User user) throws NotFoundException {
        String sql = "UPDATE users " +
                "SET email = ?, login = ?, username = ?, birthday = ? " +
                "WHERE user_id = ? " +
                "AND NOT EXISTS (SELECT 1 FROM users WHERE (email = ? OR login = ?) AND user_id != ?); ";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                dateFormat.format(user.getBirthday()),
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getId());
        return getUser(user.getId());
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * " +
                "FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    public User getUser(Integer id) throws NotFoundException {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE user_id = ?; ";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if(userRows.next()) {
            return User.builder()
                    .id(id)
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("username"))
                    .birthday(LocalDate.parse(Objects.requireNonNull(userRows.getString("birthday")), dateFormat))
                    .friends(getFriends(id))
                    .likedFilms(getLikedFilms(id))
                    .build();
        } else {
            throw new NotFoundException(String.format("Пользователь с идентификатором %d не найден.", id));
        }
    }

    public List<User> getUsers(Set<Integer> id) {
        String sql =
                "SELECT * " +
                        "FROM users" +
                        "WHERE user_id IN (?);";
        return jdbcTemplate.query(sql, id.toArray(), (rs, rowNum) -> makeUser(rs));
    }

    public Set<Integer> getFriends(Integer userId) {
        String sql = "SELECT user_id_2 " +
                "FROM friends AS f " +
                "LEFT JOIN friend_status AS fs ON f.status_id = fs.status_id " +
                "WHERE f.user_id_1 = ? " +
                "AND fs.status = 'confirmed'; ";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, userId));
    }

    public boolean dbContainsUser(Integer userId) {
        String sql = "SELECT EXISTS(SELECT * FROM USERS where USER_ID = ?); ";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId));
    }

    public Map<Integer, Integer> getFriendsStatus(Integer userId1, Integer userId2) {
        String sql = "SELECT user_id_1, status_id " +
                "FROM friends " +
                "WHERE (user_id_1 = ? AND user_id_2 = ?) OR " +
                "(user_id_1 = ? AND user_id_2 = ?); ";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userId1, userId2, userId2, userId1);
        Map<Integer, Integer> resultMap = new HashMap<>();
        // Преобразовать результат в HashMap
        for (Map<String, Object> row : rows) {
            Integer userId = (Integer) row.get("user_id_1");
            Integer statusId = (Integer) row.get("status_id");
            resultMap.put(userId, statusId);
        }
        return resultMap;
    }

    public void updateFriendStatus(Integer userId1, Integer userId2, Integer statusId) {
        String sql = "UPDATE friends " +
                "SET status_id = ? " +
                "WHERE user_id_1 = ? AND user_id_2 = ?; ";
        jdbcTemplate.update(sql, statusId, userId1, userId2);
    }

    public void addFriendStatus(Integer userId1, Integer userId2, Integer statusId) {
        String sql = "INSERT INTO friends (user_id_1, user_id_2, status_id) " +
                "VALUES ( ?, ?, ? ); ";
        jdbcTemplate.update(sql, userId1, userId2, statusId);
    }

    public Set<Integer> getLikedFilms(Integer userId) {
        String sql = "SELECT film_id " +
                "FROM liked_films " +
                "WHERE user_id = ?; ";

        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, userId));
    }

    public void setLike(Integer userId, Integer filmId) {
        String sql =
                "SET @u = ?; " +
                        "SET @f = ?; " +
                        "INSERT INTO liked_films (user_id, film_id) " +
                        "SELECT @u, @f " +
                        "WHERE NOT EXISTS (SELECT 1 FROM liked_films WHERE user_id = @u AND film_id = @f); ";
        jdbcTemplate.update(sql, userId, filmId);
    }

    public void removeLike(Integer userId, Integer filmId) {
        String sql = "DELETE FROM liked_films " +
                "WHERE user_id = ? AND film_id = ?; ";
        jdbcTemplate.update(sql, userId, filmId);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("username");
        LocalDate birthday = LocalDate.parse(rs.getString("birthday"), DateTimeFormatter.ISO_LOCAL_DATE);
        Set<Integer> friends = getFriends(id);
        Set<Integer> likedFilms = getLikedFilms(id);

        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .friends(friends)
                .likedFilms(likedFilms)
                .build();
    }

}
