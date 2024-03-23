package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BirthdayValidator.class)
public @interface Birthday {

    public String message() default "The date being checked is older than now.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
