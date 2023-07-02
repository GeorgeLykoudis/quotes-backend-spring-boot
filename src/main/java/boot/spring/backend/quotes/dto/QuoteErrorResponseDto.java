package boot.spring.backend.quotes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author George Lykoudis
 * @date 7/2/2023
 */
public class QuoteErrorResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public QuoteErrorResponseDto() {}

    private QuoteErrorResponseDto(String error) {
        this.error = error;
    }

    private QuoteErrorResponseDto(List<String> errors) {
        this.errors = errors;
    }

    public static QuoteErrorResponseDto getErrorResponse(String error) {
        return new QuoteErrorResponseDto(error);
    }

    public static QuoteErrorResponseDto getErrorsResponse(List<String> error) {
        return new QuoteErrorResponseDto(error);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
