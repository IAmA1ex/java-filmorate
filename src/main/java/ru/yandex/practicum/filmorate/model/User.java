package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.Birthday;
import ru.yandex.practicum.filmorate.validation.Login;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {

    private final int id;

    @Email
    @NotNull(message = "Email cannot be null.")
    @NotEmpty(message = "Email cannot be empty.")
    private String email;

    @Login
    @NotNull(message = "Login cannot be null.")
    @NotEmpty(message = "Login cannot be empty.")
    private String login;

    private String name;

    @Birthday
    @NotNull(message = "Birthday date cannot be null.")
    private LocalDate birthday;
}
