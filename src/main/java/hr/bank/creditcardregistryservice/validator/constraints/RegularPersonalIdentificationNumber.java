package hr.bank.creditcardregistryservice.validator.constraints;

import hr.bank.creditcardregistryservice.validator.PersonalIdentificationNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PersonalIdentificationNumberValidator.class)
public @interface RegularPersonalIdentificationNumber {

    String message() default "Personal Identification Number format not correct";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

