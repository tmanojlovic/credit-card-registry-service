package hr.bank.creditcardregistryservice.validator;

import hr.bank.creditcardregistryservice.validator.constraints.RegularName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;


public class NameValidator implements ConstraintValidator<RegularName, String> {

    private final String pattern;

    public NameValidator(@Value("${app.validation.name-pattern}") String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches(pattern);
    }
}