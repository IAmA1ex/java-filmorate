package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.MovieBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder(toBuilder = true)
public class Film {

    private final Integer id;

    @NotBlank(message = "Name cannot be blank.")
    private String name;

    @NotBlank(message = "Description cannot be blank.")
    @Size(max = 200, message = "Description must contain up to 200 characters.")
    private String description;

    @NotNull(message = "Release date cannot be null.")
    @MovieBirthday
    private LocalDate releaseDate;

    @NotNull(message = "Duration cannot be null.")
    @Positive(message = "Duration cannot be negative.")
    private int duration;

    private int likes = 0;

}
