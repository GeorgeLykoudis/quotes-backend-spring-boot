package boot.spring.backend.quotes.dto.quotes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteRequestDto extends QuoteBaseDto {

    @NotNull(message = "Text cannot be null")
    @NotBlank(message = "Text cannot be empty")
    private String text;

    public QuoteRequestDto() {
        super();
    }

    public QuoteRequestDto(Long id) {
        super(id);
    }

    public static QuoteRequestDtoBuilder builder() {
        return new QuoteRequestDtoBuilder();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static class QuoteRequestDtoBuilder {
        private final QuoteRequestDto instance = new QuoteRequestDto();

        private QuoteRequestDtoBuilder() {}

        public QuoteRequestDto build() {
            return this.instance;
        }

        public QuoteRequestDtoBuilder id(Long id) {
            instance.setId(id);
            return this;
        }

        public QuoteRequestDtoBuilder text(String text) {
            instance.text = text;
            return this;
        }
    }
}
