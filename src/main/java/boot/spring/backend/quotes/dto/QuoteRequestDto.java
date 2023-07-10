package boot.spring.backend.quotes.dto;

import boot.spring.backend.quotes.model.QuoteEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public class QuoteRequestDto {
    private String author;

    @NotNull(message = "Text cannot be null")
    @NotBlank(message = "Text cannot be empty")
    private String text;

    public QuoteRequestDto() {}

    public QuoteRequestDto(QuoteEntity quoteEntity) {
        this.author = quoteEntity.getAuthor();
        this.text = quoteEntity.getText();
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
}
