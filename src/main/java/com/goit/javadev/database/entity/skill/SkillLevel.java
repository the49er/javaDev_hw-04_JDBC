package com.goit.javadev.database.entity.skill;

import com.goit.javadev.database.exception.NoSuchLevelException;

public enum SkillLevel {
    JUNIOR("Junior"),
    MIDDLE("Middle"),
    SENIOR("Senior");
    String text;


    SkillLevel(String text) {
        this.text = text;
    }

    public String getLevel() {
        switch (this) {
            case JUNIOR:
                return "Junior";

            case MIDDLE:
                return "Middle";

            case SENIOR:
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

    public static String getMsgForException() {
        StringBuilder sb = new StringBuilder();

        for (SkillLevel sl : SkillLevel.values()) {
            sb.append(sl.getLevel());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}
