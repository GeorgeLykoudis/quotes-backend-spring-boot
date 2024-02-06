package boot.spring.backend.quotes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteRequestDto implements Serializable {
    private Long id;
    private String author;

    @NotNull(message = "Text cannot be null")
    @NotBlank(message = "Text cannot be empty")
    private String text;

    public QuoteRequestDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
