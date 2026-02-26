package models.player;

public enum Editor {
    ADMIN("admin"),
    SUPERVISOR("supervisor"),
    USER("user");

    private final String value;

    Editor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}