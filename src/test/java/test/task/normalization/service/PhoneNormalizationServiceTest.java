package test.task.normalization.service;

import org.junit.jupiter.api.Test;
import test.task.normalization.exception.ServiceException;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNormalizationServiceTest {

    PhoneNormalizationService phoneNormalizationService = new PhoneNormalizationService();

    @Test
    void normalize() {
        String rawPhone = "8 - (999) - 888 - 77 - 66";
        String result = phoneNormalizationService.normalize(rawPhone);
        assertEquals("+79998887766", result);
    }

    @Test
    void validatePhone_valid() {
        String validPhone = "79998886677";
        assertDoesNotThrow(() -> phoneNormalizationService.validatePhone(validPhone));
    }

    @Test
    void validatePhone_badLength() {
        String shortPhone = "7999";
        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> phoneNormalizationService.validatePhone(shortPhone)
        );

        String expectedMessage = "RF mobile phone should be 11 digits long, but this has 4 digits";
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void validatePhone_badPrefix() {
        String shortPhone = "09998886677";
        ServiceException thrown = assertThrows(
                ServiceException.class,
                () -> phoneNormalizationService.validatePhone(shortPhone)
        );

        String expectedMessage = "RF mobile phone should start with '79', but this isn't";
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void replaceFirst8With7IfNeeded() {
        String phone = "89998886677";
        String result = phoneNormalizationService.replaceFirst8With7IfNeeded(phone);
        assertEquals("79998886677", result);
    }
}