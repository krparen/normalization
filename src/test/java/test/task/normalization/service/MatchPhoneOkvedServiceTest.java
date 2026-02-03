package test.task.normalization.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.task.normalization.client.OkvedRegistryClient;
import test.task.normalization.dto.FlatOkvedDto;
import test.task.normalization.dto.MatchPhoneOkvedResultDto;
import test.task.normalization.dto.OkvedDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchPhoneOkvedServiceTest {

    @Mock
    private PhoneNormalizationService phoneNormalizationService;
    @Mock
    private OkvedRegistryClient okvedRegistryClient;
    @InjectMocks
    private MatchPhoneOkvedService matchPhoneOkvedService;

    private static FlatOkvedDto childOkved;
    private static FlatOkvedDto parentOkved;

    @BeforeAll
    public static void init(){
        childOkved = new FlatOkvedDto();
        childOkved.setCode("88.8");
        childOkved.setCodeNoDots("888");
        childOkved.setName("Три восьмёрки");

        parentOkved = new FlatOkvedDto();
        parentOkved.setCode("88");
        parentOkved.setCodeNoDots("88");
        parentOkved.setName("Восемь восемь");
    }

    @Test
    void matchPhoneToOkveds_longerMatch() {
        List<FlatOkvedDto> okveds = List.of(parentOkved, childOkved);
        String phone = "79995554888";
        String normalizedPhone = "+" + phone;

        when(phoneNormalizationService.normalize(phone)).thenReturn(normalizedPhone);
        when(okvedRegistryClient.getOkvedsFromGithub()).thenReturn(okveds);

        MatchPhoneOkvedResultDto result = matchPhoneOkvedService.matchPhoneToOkveds(phone);

        assertEquals(3, result.getMatchLength());
        assertEquals(childOkved.getCode(), result.getShortOkvedDto().getCode());
        assertEquals(childOkved.getName(), result.getShortOkvedDto().getName());
        assertEquals(normalizedPhone, result.getNormalizedPhone());
    }

    @Test
    void matchPhoneToOkveds_fullMatchAfterNotFullMatch() {
        // порядок важен - проверяем кейс, когда "полный" мэтч должен заместить в результате неполный
        List<FlatOkvedDto> okveds = List.of(childOkved, parentOkved);

        String phone = "79995554488";
        String normalizedPhone = "+" + phone;

        when(phoneNormalizationService.normalize(phone)).thenReturn(normalizedPhone);
        when(okvedRegistryClient.getOkvedsFromGithub()).thenReturn(okveds);

        MatchPhoneOkvedResultDto result = matchPhoneOkvedService.matchPhoneToOkveds(phone);

        assertEquals(2, result.getMatchLength());
        assertEquals(parentOkved.getCode(), result.getShortOkvedDto().getCode());
        assertEquals(parentOkved.getName(), result.getShortOkvedDto().getName());
        assertEquals(normalizedPhone, result.getNormalizedPhone());
    }

    @Test
    void matchPhoneToOkveds_noMatch() {
        List<FlatOkvedDto> okveds = List.of(childOkved, parentOkved);

        String phone = "79995551111";
        String normalizedPhone = "+" + phone;

        when(phoneNormalizationService.normalize(phone)).thenReturn(normalizedPhone);
        when(okvedRegistryClient.getOkvedsFromGithub()).thenReturn(okveds);

        MatchPhoneOkvedResultDto result = matchPhoneOkvedService.matchPhoneToOkveds(phone);

        assertNull(result.getMatchLength());
        assertNull(result.getShortOkvedDto());
        assertEquals(normalizedPhone, result.getNormalizedPhone());
    }
}