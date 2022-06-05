package com.goit.javadev.tables.entity.skill;

import com.goit.javadev.exception.NoSuchLanguageException;

public enum ProgramLang {
    java("Java"),
    cSharp("C#"),
    cPlus("C++"),
    javaScript("JavaScript");
    private String text;

    ProgramLang(String text){
        this.text = text;
    }


    public String getProgLang() {
        switch (this) {
            case java:
                return "Java";

            case cSharp:
                return "C#";

            case cPlus:
                return "C++";

            case javaScript:
                return "JavaScript";

            default:
                return null;
        }
    }

    public static String getFromString(String lang){
        for (ProgramLang pl : ProgramLang.values()) {
            if(pl.text.equalsIgnoreCase(lang)) {
                return pl.getProgLang();
            }
        }
        throw new NoSuchLanguageException(lang);
    }

    public static String getMsg(){
        StringBuilder sb = new StringBuilder();

        for(ProgramLang pl: ProgramLang.values()){
            sb.append(pl.getProgLang());
            sb.append(", ");
        }
        String result = sb.substring(0, sb.length()-2);
        return result;
    }
}


