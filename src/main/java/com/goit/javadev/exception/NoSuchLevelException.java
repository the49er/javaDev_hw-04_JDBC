package com.goit.javadev.exception;

import com.goit.javadev.tables.entity.skill.SkillLevel;

public class NoSuchLevelException extends RuntimeException {
    public NoSuchLevelException(String str) {
        super("No such Enum value: " + str +
                "\nAccepted values are: " + SkillLevel.getMsg() + " (ignoreCaseAble as well)");
    }
}
