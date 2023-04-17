package hr.bank.creditcardregistryservice.validator;

import hr.bank.creditcardregistryservice.validator.constraints.RegularPersonalIdentificationNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PersonalIdentificationNumberValidator implements ConstraintValidator<RegularPersonalIdentificationNumber, String> {

    private final ConstraintValidator<RegularPersonalIdentificationNumber, String> delegate;

    public PersonalIdentificationNumberValidator(ConstraintValidator<RegularPersonalIdentificationNumber, String> delegate){
        this.delegate = delegate;
    }

    @Override
    public boolean isValid(String personalIdentificationNumber, ConstraintValidatorContext constraintValidatorContext) {
        return delegate.isValid(personalIdentificationNumber, constraintValidatorContext);
    }
}