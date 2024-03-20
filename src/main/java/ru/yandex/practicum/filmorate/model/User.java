package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.Login;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {

    private final Integer id;

    @Email
    @NotBlank(message = "Email cannot be blank.")
    private String email;

    @Login
    @NotBlank(message = "Login cannot be blank.")
    private String login;

    private String name;

    @Past
    @NotNull(message = "Birthday date cannot be null.")
    private LocalDate birthday;
}
