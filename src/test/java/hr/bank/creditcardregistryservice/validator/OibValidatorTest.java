package hr.bank.creditcardregistryservice.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OibValidatorTest {

    private final OibValidator oibValidator = new OibValidator();

    @Test
    public void testRegularOib() {
        assertTrue(oibValidator.isValid("34168888890", null));

    }

    @Test
    public void testWrongLengthOib() {
        assertFalse(oibValidator.isValid("3416888889", null));
    }

    @Test
    public void testWrongOibChecksum() {
        assertFalse(oibValidator.isValid("34168888898", null));
    }

}