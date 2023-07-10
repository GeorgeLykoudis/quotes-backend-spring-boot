package boot.spring.backend.quotes.exception;

/**
 * @author George Lykoudis
 * @date 7/1/2023
 */
public enum ErrorConstants {
    QUOTE_NOT_FOUND("Could not find quote id ", 0),
    QUOTE_ALREADY_EXIST("Quote already exist.", 1),
    EMPTY_BODY_MESSAGE("Request body cannot be null or empty.", 2),
    GENERAL_EXCEPTION_MESSAGE("An Error occurred", 3),
    TYPE_MISMATCH_EXCEPTION_MESSAGE("Type miss matched", 4),
    MISSING_REQUEST_PARAMETER("Missing Request Parameter", 5);

    String text;
    int code;

    ErrorConstants(String text, int code) {
        this.text = text;
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }
}
