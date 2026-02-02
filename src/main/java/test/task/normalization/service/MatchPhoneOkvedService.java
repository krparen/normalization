package test.task.normalization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.task.normalization.dto.MatchPhoneOkvedResultDto;

@Service
@RequiredArgsConstructor
public class MatchPhoneOkvedService {

    private final PhoneNormalizationService phoneNormalizationService;

    public MatchPhoneOkvedResultDto matchPhoneToOkveds(String rawPhone) {
        String normalizedPhone = phoneNormalizationService.normalize(rawPhone);

        return null;
    }
}
