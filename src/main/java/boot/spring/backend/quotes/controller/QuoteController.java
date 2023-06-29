package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.model.QuoteEntity;
import boot.spring.backend.quotes.service.QuoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public ResponseEntity<QuoteEntity> getQuoteById(@Valid @PathVariable("id") Long id) {
        QuoteEntity quote = quoteService.findQuoteById(id);
        return ResponseEntity.ok(quote);
    }
}
