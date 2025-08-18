package org.hrmanage.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaveType {
    PATERNITY, UNPAID, CASUAL, MATERNITY, SICK, ANNUAL;

    @JsonCreator
    public static LeaveType fromString(String key) {
        return LeaveType.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
