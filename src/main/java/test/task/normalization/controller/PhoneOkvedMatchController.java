package test.task.normalization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.task.normalization.dto.MatchPhoneOkvedResultDto;
import test.task.normalization.service.MatchPhoneOkvedService;

@RestController
@RequiredArgsConstructor
public class PhoneOkvedMatchController {

    private final MatchPhoneOkvedService matchPhoneOkvedService;

    @GetMapping("/match")
    public MatchPhoneOkvedResultDto match(@RequestParam String phone) {
        return matchPhoneOkvedService.matchPhoneToOkveds(phone);
    }
}
