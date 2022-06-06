package com.goit.javadev.tables.entity.skill;

import com.goit.javadev.exception.NoSuchLanguageException;
import com.goit.javadev.exception.NoSuchLevelException;

public enum SkillLevel {
    junior("Junior"),
    middle("Middle"),
    senior("Senior");
    private String text;


    SkillLevel(String text){
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
        throw new NoSuchLanguageException(lang);
    }

    public static String getFromString(String lang) {
        return getEnumFromString(lang).getLevel();
    }

    public static String getMsg(){
        StringBuilder sb = new StringBuilder();

        for(SkillLevel sl: SkillLevel.values()){
            sb.append(sl.getLevel());
            sb.append(", ");
        }
        String result = sb.substring(0, sb.length()-2);
        return result;
    }
}
