package models.player;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    SUPERVISOR("supervisor"),
    USER("user"),
    ADMIN("admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Role from(String raw) {
        if (raw == null) {
            return null;
        }
        String v = raw.trim().toLowerCase();
        for (Role r : values()) {
            if (r.value.equals(v)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + raw);
    }
}