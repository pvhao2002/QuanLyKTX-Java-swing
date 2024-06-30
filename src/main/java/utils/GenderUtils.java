package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GenderUtils {
    public String getGenderName(String gender) {
        if ("Nam".equalsIgnoreCase(gender)) {
            return "MALE";
        } else if ("Nữ".equalsIgnoreCase(gender)) {
            return "FEMALE";
        }
        return "OTHER";
    }

    public String convertGenderName(String gender) {
        if ("MALE".equalsIgnoreCase(gender)) {
            return "Nam";
        } else if ("FEMLAE".equalsIgnoreCase(gender)) {
            return "Nữ";
        }
        return "Khác";
    }
}
