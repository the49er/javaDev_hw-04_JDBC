package com.goit.javadev.database.exception;

import com.goit.javadev.database.entity.skill.SkillLevel;

public class NoSuchLevelException extends RuntimeException {
    public NoSuchLevelException(String str) {
        super("No such Enum value: " + str +
                "\nAccepted values are: " + SkillLevel.getMsgForException() + " (ignoreCaseAble as well)");
    }
}
