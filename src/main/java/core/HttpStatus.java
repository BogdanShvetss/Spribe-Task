package core;

public enum HttpStatus {

    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    NO_CONTENT(204);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}