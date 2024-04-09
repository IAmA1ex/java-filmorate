package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

public class FilmorateApplicationMemoryTest extends FilmorateApplicationTests<InMemoryFilmStorage, InMemoryUserStorage> {

    @Autowired
    public FilmorateApplicationMemoryTest(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        super(filmStorage, userStorage);
    }

    @BeforeEach
    public void setUp() {
        filmStorage.counterId = 1;
        filmStorage.films.clear();

        userStorage.counterId = 1;
        userStorage.users.clear();
    }
}
