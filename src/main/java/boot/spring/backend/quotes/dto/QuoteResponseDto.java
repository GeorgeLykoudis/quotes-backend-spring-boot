package boot.spring.backend.quotes.dto;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public class QuoteResponseDto extends QuoteRequestDto {
    private Long id;

    public QuoteResponseDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
