package test.task.normalization.dto;

import lombok.Data;

@Data
public class MatchPhoneOkvedResultDto {
    private String normalizedPhone;
    private ShortOkvedDto shortOkvedDto;
    private Integer matchLength;
}
