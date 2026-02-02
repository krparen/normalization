package test.task.normalization.dto;

import lombok.Data;

@Data
public class MatchPhoneOkvedInfo {
    private FlatOkvedDto okved = null;
    private String bestMatch = "";
    private boolean fullMatch = false;

    public int getMatchLength() {
        return bestMatch.length();
    }
}
