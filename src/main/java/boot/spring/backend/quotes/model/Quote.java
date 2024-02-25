package boot.spring.backend.quotes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedBy;

import java.time.Instant;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@Entity
@Table(name = "quotes")
public class Quote extends Audit {
    private String text;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    public Quote() { super(); }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
            this.instance.setId(id);
            return this;
        }

        public QuoteBuilder text(String text) {
            this.instance.text = text;
            return this;
        }

        public QuoteBuilder createdAt(Instant date) {
            this.instance.setCreatedAt(date);
            return this;
        }

        public QuoteBuilder lastModifiedAt(Instant date) {
            this.instance.setLastModifiedAt(date);
            return this;
        }

        public QuoteBuilder createdBy(String createdBy) {
            this.instance.createdBy = createdBy;
            return this;
        }
    }
}
