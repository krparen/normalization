package test.task.normalization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchPhoneOkvedResultDto {
    private String normalizedPhone;
    private ShortOkvedDto okved;
    private Integer matchLength;

    public static MatchPhoneOkvedResultDto phoneOnly(String phone) {
       return new MatchPhoneOkvedResultDto(phone, null, null);
    }
}
