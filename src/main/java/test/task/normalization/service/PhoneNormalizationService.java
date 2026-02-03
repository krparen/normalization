package test.task.normalization.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import test.task.normalization.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhoneNormalizationService {

    private static final int RF_MOBILE_PHONE_LENGTH = 11;

    private static final String RF_MOBILE_PHONE_PREFIX = "79";

    private static final String PHONE_LENGTH_ERROR_TEMPLATE =
            "RF mobile phone should be %s digits long, but this has %s digits";

    private static final String PHONE_PREFIX_ERROR_MESSAGE =
            "RF mobile phone should start with '79', but this isn't";

    /**
     * В условии написано, что ввод без ограничений - поэтому оставляю из введённого только цифры.
     * @param rawPhone - ещё не нормализованный телефон.
     * @return Нормализованный телефон.
     */
    public String normalize(String rawPhone) {
        if (!StringUtils.hasText(rawPhone)) {
            throw new ServiceException("Empty or null phone cannot be normalized", HttpStatus.BAD_REQUEST);
        }

        String phoneOnlyDigits = replaceFirst8With7IfNeeded(rawPhone.replaceAll("\\D", ""));

        validatePhone(phoneOnlyDigits);

        return "+" + phoneOnlyDigits;
    }

    void validatePhone(String phone) {
        List<String> errors = new ArrayList<>();

        if (phone.length() != RF_MOBILE_PHONE_LENGTH) {
            String errorMessage = String.format(PHONE_LENGTH_ERROR_TEMPLATE, RF_MOBILE_PHONE_LENGTH, phone.length());
            errors.add(errorMessage);
        }

        if (!phone.startsWith(RF_MOBILE_PHONE_PREFIX)) {
            errors.add(PHONE_PREFIX_ERROR_MESSAGE);
        }

        if (!errors.isEmpty()) {
            String fullErrorMessage = String.join("; ", errors);
            throw new ServiceException(fullErrorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    String replaceFirst8With7IfNeeded(String phone) {
        if (phone.startsWith("8")) {
            return "7" + phone.substring(1);
        }

        return phone;
    }
}
