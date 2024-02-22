package boot.spring.backend.quotes.dto.quotes;

import java.io.Serializable;
import java.util.List;

/**
 * @author George Lykoudis
 * @date 7/11/2023
 */
public class QuoteResponsePaginationDto implements Serializable {
    private List<QuoteResponseDto> quotes;
    private long totalQuotes;
    private int pageSize;
    private int page;

    public List<QuoteResponseDto> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<QuoteResponseDto> quotes) {
        this.quotes = quotes;
    }

    public long getTotalQuotes() {
        return totalQuotes;
    }

    public void setTotalQuotes(long totalQuotes) {
        this.totalQuotes = totalQuotes;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
