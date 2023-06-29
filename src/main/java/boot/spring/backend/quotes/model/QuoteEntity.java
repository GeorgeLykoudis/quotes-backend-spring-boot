package boot.spring.backend.quotes.model;

import boot.spring.backend.quotes.dto.QuoteRequestDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@Entity
@Table(name = "quote")
public class QuoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String author;
    private String text;

    public QuoteEntity() {}

    private QuoteEntity(QuoteRequestDto quoteRequestDto) {
        this.author = quoteRequestDto.getAuthor();
        this.text = quoteRequestDto.getText();
    }

    public static QuoteEntity createQuoteEntity(QuoteRequestDto quoteRequestDto) {
        return new QuoteEntity(quoteRequestDto);
    }

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
