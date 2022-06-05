package com.goit.javadev.tables.entity.skill;

public enum ProgramLang {
    java,
    cSharp,
    cPlus,
    javaScript;


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
}


