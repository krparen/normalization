package test.task.normalization.dto;

import lombok.Data;

import java.util.List;

@Data
public class OkvedDto {
    private String code;
    private String name;
    private List<OkvedDto> items;
}
