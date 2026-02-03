package test.task.normalization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import test.task.normalization.controller.advice.GlobalExceptionHandler;
import test.task.normalization.dto.MatchPhoneOkvedResultDto;
import test.task.normalization.dto.PhoneOkvedRequestDto;
import test.task.normalization.dto.ShortOkvedDto;
import test.task.normalization.exception.ServiceException;
import test.task.normalization.service.MatchPhoneOkvedService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhoneOkvedMatchController.class)
@Import(GlobalExceptionHandler.class)
class PhoneOkvedMatchControllerTest {

    @MockitoBean
    private MatchPhoneOkvedService matchPhoneOkvedService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void match() throws Exception {
        String phone = "79998887788";
        String normalizedPhone = "+" + phone;

        ShortOkvedDto matchingOkved = new ShortOkvedDto();
        matchingOkved.setCode("88");
        matchingOkved.setName("Восемь восемь");

        MatchPhoneOkvedResultDto expectedResult = new MatchPhoneOkvedResultDto();
        expectedResult.setOkved(matchingOkved);
        expectedResult.setNormalizedPhone(normalizedPhone);
        expectedResult.setMatchLength(2);

        PhoneOkvedRequestDto request = new PhoneOkvedRequestDto(phone);
        String requestAsString = new ObjectMapper().writeValueAsString(request);


        when(matchPhoneOkvedService.matchPhoneToOkveds(anyString())).thenReturn(expectedResult);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/match")
                        .content(requestAsString)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchLength").value(2))
                .andExpect(jsonPath("$.normalizedPhone").value(normalizedPhone))
                .andExpect(jsonPath("$.okved.code").value(matchingOkved.getCode()))
                .andExpect(jsonPath("$.okved.name").value(matchingOkved.getName()));
    }

    @Test
    void matchFailed() throws Exception {
        String phone = "79998887788";

        PhoneOkvedRequestDto request = new PhoneOkvedRequestDto(phone);
        String requestAsString = new ObjectMapper().writeValueAsString(request);


        ServiceException e = new ServiceException("some message", HttpStatus.BAD_REQUEST);
        when(matchPhoneOkvedService.matchPhoneToOkveds(anyString())).thenThrow(e);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/match")
                                .content(requestAsString)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(e.getMessage()));
    }
}