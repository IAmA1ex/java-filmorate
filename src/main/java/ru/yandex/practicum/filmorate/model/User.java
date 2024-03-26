package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.Login;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {

    private final Integer id;

    @Email(message = "The email must have the format of an email address.")
    @NotBlank(message = "The email cannot be blank.")
    private String email;

    @Login
    @NotBlank(message = "The login cannot be blank.")
    private String login;

    private String name;

    @Past(message = "The birthday date cannot be in the future.")
    @NotNull(message = "The birthday date cannot be null.")
    private LocalDate birthday;

    private final Set<Integer> friends = new HashSet<>();

    private final Set<Integer> likedFilms = new HashSet<>();

}
