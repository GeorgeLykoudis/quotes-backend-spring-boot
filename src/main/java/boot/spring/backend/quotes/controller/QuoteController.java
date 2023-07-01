package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.model.QuoteEntity;
import boot.spring.backend.quotes.service.QuoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
        QuoteEntity quote = quoteService.saveQuote(quoteRequestDto);
        QuoteResponseDto quoteResponse = QuoteResponseDto.createQuote(quote);
        return ResponseEntity.status(HttpStatus.CREATED).body(quoteResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteResponseDto> getQuoteById(@Valid @PathVariable("id") Long id) {
        QuoteEntity quote = quoteService.findQuoteById(id);
        QuoteResponseDto quoteResponse = QuoteResponseDto.createQuote(quote);
        return ResponseEntity.ok(quoteResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<QuoteResponseDto> updateQuoteById(@Valid @PathVariable("id") Long id,
                                                            @Valid @RequestBody QuoteRequestDto quoteRequestDto) {
        QuoteEntity quote = quoteService.updateQuoteById(id, quoteRequestDto);
        QuoteResponseDto quoteResponse = QuoteResponseDto.createQuote(quote);
        return ResponseEntity.ok(quoteResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuoteById(@Valid @PathVariable("id") Long id) {
        quoteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<QuoteResponseDto>> findQuotes() {
        List<QuoteResponseDto> quotes = quoteService.findAllDtos();
        if (quotes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quotes);
    }

    @GetMapping("/page/{page}/pageSize/{pageSize}")
    public ResponseEntity<List<QuoteResponseDto>> findQuotes(@PathVariable("page") int page,
                                                             @PathVariable("pageSize") int pageSize) {
        List<QuoteResponseDto> quotes = quoteService.findAllDtos(page, pageSize);
        if (quotes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quotes);
    }

    @GetMapping("/random")
    public ResponseEntity<QuoteResponseDto> findRandomQuote() {
        QuoteEntity quote = quoteService.findRandomQuote();
        QuoteResponseDto quoteResponse = QuoteResponseDto.createQuote(quote);
        return ResponseEntity.ok(quoteResponse);
    }
}
