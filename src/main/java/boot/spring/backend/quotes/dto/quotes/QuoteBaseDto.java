package boot.spring.backend.quotes.dto.quotes;

import java.io.Serializable;

public abstract class QuoteBaseDto implements Serializable {

  private Long id;

  public QuoteBaseDto() {}

  public QuoteBaseDto(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
