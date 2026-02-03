package test.task.normalization.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import test.task.normalization.client.OkvedRegistryClient;
import test.task.normalization.dto.FlatOkvedDto;
import test.task.normalization.dto.MatchPhoneOkvedInfo;
import test.task.normalization.dto.MatchPhoneOkvedResultDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchPhoneOkvedService {

    private final PhoneNormalizationService phoneNormalizationService;
    private final OkvedRegistryClient okvedRegistryClient;

    public MatchPhoneOkvedResultDto matchPhoneToOkveds(String rawPhone) {
        String normalizedPhone = phoneNormalizationService.normalize(rawPhone);
        String phoneInReverse = StringUtils.reverse(normalizedPhone);
        List<FlatOkvedDto> okveds = okvedRegistryClient.getOkvedsFromGithub();
        MatchPhoneOkvedInfo matchingInfo = new MatchPhoneOkvedInfo();

        okveds.forEach(item -> {
            String commonPrefix = StringUtils.getCommonPrefix(phoneInReverse, item.getCodeNoDots());
            if (StringUtils.isNotEmpty(commonPrefix)) {
                updateBestMatchIfNeeded(item, commonPrefix, matchingInfo);
            }
        });

        if (matchingInfo.noMatchHappened()) {
            return MatchPhoneOkvedResultDto.phoneOnly(normalizedPhone);
        }

        MatchPhoneOkvedResultDto result = new MatchPhoneOkvedResultDto();
        result.setMatchLength(matchingInfo.getMatchLength());
        result.setNormalizedPhone(normalizedPhone);
        result.setShortOkvedDto(matchingInfo.getOkved().toShort());

        return result;
    }

    private static void updateBestMatchIfNeeded(FlatOkvedDto item, String commonPrefix, MatchPhoneOkvedInfo matchingInfo) {
        // Если случился "полный" мэтч - ОКВЭД совпал полностью с началом перевёрнутого номера,
        // и при этом на данный момент самый лучший мэтч - той же длины, но не "полный".
        // Тогда запоминаем "полный" мэтч - он более приоритетный.
        if (commonPrefix.length() == matchingInfo.getMatchLength()
                && !matchingInfo.isFullMatch()
                && commonPrefix.length() == item.getCodeNoDots().length()
        ) {
            matchingInfo.setOkved(item);
            matchingInfo.setBestMatch(commonPrefix);
            matchingInfo.setFullMatch(true);
        // Обычный кейс - встретили более длинный мэтч.
        } else if (commonPrefix.length() > matchingInfo.getMatchLength()) {
            matchingInfo.setOkved(item);
            matchingInfo.setBestMatch(commonPrefix);
            matchingInfo.setFullMatch(commonPrefix.length() == item.getCodeNoDots().length());
        }
    }
}
