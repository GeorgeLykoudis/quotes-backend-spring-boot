package boot.spring.backend.quotes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String author;
    private String text;

    public Quote() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quote)) return false;

        Quote that = (Quote) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    public static QuoteBuilder builder() {
        return new QuoteBuilder();
    }

    public static class QuoteBuilder {
        private final Quote instance = new Quote();

        public Quote build() {
            return this.instance;
        }

        public QuoteBuilder id(Long id) {
            this.instance.id = id;
            return this;
        }

        public QuoteBuilder author(String author) {
            instance.author = author;
            return this;
        }

        public QuoteBuilder text(String text) {
            this.instance.text = text;
            return this;
        }
    }
}
