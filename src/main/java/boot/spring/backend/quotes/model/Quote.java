package boot.spring.backend.quotes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@Entity
@Table(name = "quotes")
public class Quote extends Audit {
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Quote() { super(); }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

        public QuoteBuilder user(User user) {
            instance.user = user;
            return this;
        }

        public QuoteBuilder text(String text) {
            this.instance.text = text;
            return this;
        }
    }
}
