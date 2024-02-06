package boot.spring.backend.quotes.dto;

import java.io.Serializable;

/**
 * @author George Lykoudis
 * @date 7/2/2023
 */
public class QuoteErrorResponseDto implements Serializable {
    private int errorCode;

    public QuoteErrorResponseDto() {}

    private QuoteErrorResponseDto(int errorCode) {
        this.errorCode = errorCode;
    }

    public static QuoteErrorResponseDto of(int errorCode) {
        return new QuoteErrorResponseDto(errorCode);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
