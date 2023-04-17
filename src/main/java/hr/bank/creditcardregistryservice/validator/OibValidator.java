package hr.bank.creditcardregistryservice.validator;

import hr.bank.creditcardregistryservice.validator.constraints.RegularPersonalIdentificationNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OibValidator implements ConstraintValidator<RegularPersonalIdentificationNumber, String> {
    /*
    Downloaded from: https://github.com/domagojpa/oib-validation/blob/main/Java/OibValidation.java
    */
    public static final int NO_ERROR = 0;
    public static final int ERR_INVALID_LENGTH = 1;
    public static final int ERR_INVALID_FORMAT = 2;
    public static final int ERR_INVALID_CONTROL = 3;

    private static final int asciiDigitsOffset = '0';

    private boolean checkOIB(String oib) {
        return checkOIBState(oib) == NO_ERROR;
    }

    private int checkOIBState(String oib) {
        if (oib.length() != 11) {
            return ERR_INVALID_LENGTH;
        }

        char[] chars = oib.toCharArray();

        int a = 10;
        for (int i = 0; i < 10; i++) {
            char c = chars[i];
            if (c < '0' || c > '9') {
                return ERR_INVALID_FORMAT;
            }
            a = a + (c - asciiDigitsOffset);
            a = a % 10;
            if (a == 0) {
                a = 10;
            }
            a *= 2;
            a = a % 11;
        }
        int kontrolni = 11 - a;
        kontrolni = kontrolni % 10;

        if (kontrolni != (chars[10] - asciiDigitsOffset)) {
            return ERR_INVALID_CONTROL;
        }
        return NO_ERROR;
    }


    @Override
    public boolean isValid(String oib, ConstraintValidatorContext constraintValidatorContext) {
        return checkOIB(oib);
    }
}
