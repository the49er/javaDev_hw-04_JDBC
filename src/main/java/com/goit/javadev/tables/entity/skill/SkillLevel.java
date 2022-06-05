package com.goit.javadev.tables.entity.skill;

public enum SkillLevel {
    junior,
    middle,
    senior;

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
}
