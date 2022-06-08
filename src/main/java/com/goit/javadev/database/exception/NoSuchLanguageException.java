package com.goit.javadev.database.exception;

import com.goit.javadev.database.entity.skill.ProgramLang;

public class NoSuchLanguageException extends RuntimeException {
    public NoSuchLanguageException(String str) {
        super("No such Enum value: " + str +
                "\nAccepted values are: " + ProgramLang.getMsgForException() + " (ignoreCaseAble as well)");
    }
}
