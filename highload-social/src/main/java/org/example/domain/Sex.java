package org.example.domain;

public enum Sex {
    MALE,
    FEMALE;

    public static Sex from(String value) {
        for (Sex sex : Sex.values()) {
            if (sex.toString().equalsIgnoreCase(value)) {
                return sex;
            }
        }
        return null;
    }
}
