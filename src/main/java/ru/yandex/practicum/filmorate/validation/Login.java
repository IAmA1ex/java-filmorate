package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LoginValidator.class)
public @interface Login {

    public String message() default "The login cannot contain spaces.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
