package hr.bank.creditcardregistryservice.validator.constraints;


import hr.bank.creditcardregistryservice.validator.NameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = NameValidator.class)
public @interface RegularName {
    String message() default "Name format is not supported. " +
            "Name should start with capital letter, have at least 2 characters" +
            "and have no special characters/numbers";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

