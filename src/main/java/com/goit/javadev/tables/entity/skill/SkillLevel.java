package com.goit.javadev.tables.entity.skill;

import com.goit.javadev.exception.NoSuchLevelException;

public enum SkillLevel {
    junior("Junior"),
    middle("Middle"),
    senior("Senior");
    String text;


    SkillLevel(String text) {
        this.text = text;
    }

    public String getLevel() {
        switch (this) {
            case junior:
                return "Junior";

            case middle:
                return "Middle";

            case senior:
                return "Senior";

            default:
                return null;
        }
    }

    public static SkillLevel getEnumFromString(String lang) {
        for (SkillLevel sl : SkillLevel.values()) {
            if (sl.text.equalsIgnoreCase(lang)) {
                return sl;
            }
        }
        throw new NoSuchLevelException(lang);
    }

    public static String getEnumValueFromString(String lang) {
        return getEnumFromString(lang).getLevel();
    }

    public static String getMsg() {
        StringBuilder sb = new StringBuilder();

        for (SkillLevel sl : SkillLevel.values()) {
            sb.append(sl.getLevel());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}
