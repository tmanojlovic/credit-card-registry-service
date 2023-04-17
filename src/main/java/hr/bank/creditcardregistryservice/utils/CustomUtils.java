package hr.bank.creditcardregistryservice.utils;

import java.math.BigInteger;

public class CustomUtils {

    public static BigInteger personalIdentificationNumberToId(String personalIdentificationNumber) {
        if (personalIdentificationNumber.startsWith("0")) {
            return new BigInteger(personalIdentificationNumber.substring(1, personalIdentificationNumber.length() - 1));
        } else {
            return new BigInteger(personalIdentificationNumber);
        }
    }

}
