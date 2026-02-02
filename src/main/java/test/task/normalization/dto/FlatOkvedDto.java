package test.task.normalization.dto;

import lombok.Data;

@Data
public class FlatOkvedDto {
    private String code;
    private String codeNoDots;
    private String name;

    public ShortOkvedDto toShort() {
        return new ShortOkvedDto(code, name);
    }
}
