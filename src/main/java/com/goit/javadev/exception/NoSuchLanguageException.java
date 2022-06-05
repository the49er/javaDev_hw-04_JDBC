package com.goit.javadev.exception;

import com.goit.javadev.tables.entity.skill.ProgramLang;

public class NoSuchLanguageException extends RuntimeException {
    public NoSuchLanguageException(String str) {
        super("No such Enum value: " + str +
                "\nAccepted values are: " + ProgramLang.getMsg() + " (ignoreCaseAble as well)");
    }
}
