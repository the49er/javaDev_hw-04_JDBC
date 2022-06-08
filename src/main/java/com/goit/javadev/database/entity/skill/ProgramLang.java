package com.goit.javadev.database.entity.skill;

import com.goit.javadev.database.exception.NoSuchLanguageException;

public enum ProgramLang {
    JAVA("Java"),
    C_SHARP("C#"),
    C_PLUS("C++"),
    JAVA_SCRIPT("JavaScript");
    String text;

    ProgramLang(String text) {
        this.text = text;
    }


    public String getProgLang() {
        switch (this) {
            case JAVA:
                return "Java";

            case C_SHARP:
                return "C#";

            case C_PLUS:
                return "C++";

            case JAVA_SCRIPT:
                return "JavaScript";

            default:
                return null;
        }
    }

    public static ProgramLang getEnumFromString(String lang) {
        for (ProgramLang pl : ProgramLang.values()) {
            if (pl.text.equalsIgnoreCase(lang)) {
                return pl;
            }
        }
        throw new NoSuchLanguageException(lang);
    }

    public static String getFromString(String lang) {
        return getEnumFromString(lang).getProgLang();
    }

    public static String getMsgForException() {
        StringBuilder sb = new StringBuilder();

        for (ProgramLang pl : ProgramLang.values()) {
            sb.append(pl.getProgLang());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}


