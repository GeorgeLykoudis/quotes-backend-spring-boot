package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.dto.QuoteResponsePaginationDto;
import boot.spring.backend.quotes.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    public ResponseEntity<QuoteResponseDto> createQuote(@Valid @RequestBody QuoteRequestDto quoteRequestDto) {
        QuoteResponseDto quoteResponse = quoteService.saveQuote(quoteRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(quoteResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteResponseDto> getQuoteById(@Valid @PathVariable("id") Long id) {
        QuoteResponseDto quoteResponse = quoteService.findQuoteById(id);
        return ResponseEntity.ok(quoteResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<QuoteResponseDto> updateQuoteById(@Valid @RequestBody QuoteRequestDto quoteRequestDto) {
        QuoteResponseDto quoteResponse = quoteService.updateQuote(quoteRequestDto);
        return ResponseEntity.ok(quoteResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuoteById(@Valid @PathVariable("id") Long id) {
        quoteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<QuoteResponseDto>> findQuotes() {
        List<QuoteResponseDto> quotes = quoteService.findAll();
        return ResponseEntity.ok(quotes);
    }

    @GetMapping("/paginated")
    public ResponseEntity<QuoteResponsePaginationDto> findQuotes(
            @Valid @RequestParam(value = "p", required = false, defaultValue = "0") int page,
            @Valid @RequestParam(value = "s", required = false, defaultValue = "5") int pageSize) {
        QuoteResponsePaginationDto quotes = quoteService.findAll(page, pageSize);
        return ResponseEntity.ok(quotes);
    }

    @GetMapping("/random")
    public ResponseEntity<QuoteResponseDto> findRandomQuote() {
        QuoteResponseDto quoteResponse = quoteService.findRandomQuote();
        return ResponseEntity.ok(quoteResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<List<QuoteResponseDto>> findQuotesHavingText(@Valid @RequestParam("t") String text) {
        List<QuoteResponseDto> quotes = quoteService.findQuotesHavingText(text);
        return ResponseEntity.ok(quotes);
    }

    @GetMapping("/search/paginated")
    public ResponseEntity<QuoteResponsePaginationDto> findQuotesHavingText(
            @Valid @RequestParam("t") String text,
            @Valid @RequestParam(value = "p", required = false, defaultValue = "0") int page,
            @Valid @RequestParam(value = "s", required = false, defaultValue = "5") int pageSize) {
        QuoteResponsePaginationDto quotes = quoteService.findQuotesHavingText(text, page, pageSize);
        return ResponseEntity.ok(quotes);
    }
}
