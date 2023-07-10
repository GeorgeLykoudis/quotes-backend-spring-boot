package boot.spring.backend.quotes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;

/**
 * @author George Lykoudis
 * @date 7/2/2023
 */
public class QuoteErrorResponseDto {
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
