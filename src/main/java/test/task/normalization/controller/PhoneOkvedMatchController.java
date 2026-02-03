package test.task.normalization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import test.task.normalization.dto.MatchPhoneOkvedResultDto;
import test.task.normalization.dto.PhoneOkvedRequestDto;
import test.task.normalization.service.MatchPhoneOkvedService;

@RestController
@RequiredArgsConstructor
public class PhoneOkvedMatchController {

    private final MatchPhoneOkvedService matchPhoneOkvedService;

    @PostMapping("/match")
    public MatchPhoneOkvedResultDto match(@RequestBody PhoneOkvedRequestDto request) {
        return matchPhoneOkvedService.matchPhoneToOkveds(request.getPhone());
    }
}
