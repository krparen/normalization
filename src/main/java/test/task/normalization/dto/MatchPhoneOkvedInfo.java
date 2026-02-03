package test.task.normalization.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class MatchPhoneOkvedInfo {
    private FlatOkvedDto okved = null;
    private String bestMatch = "";
    private boolean fullMatch = false;

    public int getMatchLength() {
        return bestMatch.length();
    }

    public boolean noMatchHappened() {
        return StringUtils.isEmpty(bestMatch) || okved == null;
    }
}
