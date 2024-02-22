package boot.spring.backend.quotes.dto.quotes;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public class QuoteResponseDto extends QuoteBaseDto {

    private String author;
    private String text;

    public QuoteResponseDto() {
        super();
    }

    public QuoteResponseDto(Long id) {
        super(id);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static QuoteResponseDtoBuilder builder() {
        return new QuoteResponseDtoBuilder();
    }

    public static class QuoteResponseDtoBuilder {
        private final QuoteResponseDto instance = new QuoteResponseDto();

        public QuoteResponseDto build() {
            return this.instance;
        }

        public QuoteResponseDtoBuilder id(Long id) {
            this.instance.setId(id);
            return this;
        }

        public QuoteResponseDtoBuilder author(String author) {
            this.instance.author = author;
            return this;
        }

        public QuoteResponseDtoBuilder text(String text) {
            this.instance.text = text;
            return this;
        }
    }
}
