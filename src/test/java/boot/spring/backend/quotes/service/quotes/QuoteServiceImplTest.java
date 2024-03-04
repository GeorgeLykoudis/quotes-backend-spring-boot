package boot.spring.backend.quotes.service.quotes;

import boot.spring.backend.quotes.Utils;
import boot.spring.backend.quotes.dto.quotes.QuoteRequestDto;
import boot.spring.backend.quotes.dto.quotes.QuoteResponseDto;
import boot.spring.backend.quotes.dto.quotes.QuoteResponsePaginationDto;
import boot.spring.backend.quotes.exception.QuoteAlreadyExistException;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.model.Quote;
import boot.spring.backend.quotes.repository.QuoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author George Lykoudis
 * @date 7/1/2023
 */
@ExtendWith(MockitoExtension.class)
class QuoteServiceImplTest {

    @Mock
    QuoteRepository quoteRepository;

    @InjectMocks
    QuoteServiceImpl quoteService;

    @Test
    void findQuoteById_Success() {
        Long id = 1L;
        String text = "text";
        String createdBy = "email@test.com";
        Quote searchedQuote = Quote.builder()
            .id(id)
            .createdBy(createdBy)
            .text(text)
            .build();
        Utils.initValidSecurityContext();

        when(quoteRepository.findByIdAndCreatedBy(anyLong(), anyString())).thenReturn(Optional.of(searchedQuote));
        QuoteResponseDto result = quoteService.findQuoteById(id);

        verify(quoteRepository, times(1)).findByIdAndCreatedBy(anyLong(), anyString());

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(text, result.getText());
    }

    @Test
    void findQuoteById_NonExistentQuote_ThrowsQuoteNotFoundException() {
        Long id = 1L;
        Utils.initValidSecurityContext();
        when(quoteRepository.findByIdAndCreatedBy(anyLong(), anyString())).thenReturn(Optional.empty());

        assertThrows(QuoteNotFoundException.class, () -> quoteService.findQuoteById(id));
        verify(quoteRepository, times(1)).findByIdAndCreatedBy(anyLong(), anyString());
    }

    @Test
    void saveQuote_Success() {
        long id = 1L;
        String text = "text";
        String createdBy = "email@test.com";
        Quote savedQuote = Quote.builder()
            .id(id)
            .createdBy(createdBy)
            .text(text)
            .build();

        QuoteRequestDto request = QuoteRequestDto.builder()
            .text(text)
            .build();

        when(quoteRepository.existsQuoteByText(anyString())).thenReturn(false);
        when(quoteRepository.save(any(Quote.class))).thenReturn(savedQuote);
        QuoteResponseDto result = quoteService.saveQuote(request);

        verify(quoteRepository, times(1)).save(any(Quote.class));
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(request.getText(), result.getText());
    }

    @Test
    void saveQuoteThatAlreadyExists_ThrowsQuoteAlreadyExistException() {
        String text = "text";

        QuoteRequestDto request = QuoteRequestDto.builder()
            .text(text)
            .build();

        when(quoteRepository.existsQuoteByText(anyString())).thenReturn(true);
        assertThrows(QuoteAlreadyExistException.class, () -> quoteService.saveQuote(request));

        verify(quoteRepository, never()).save(any(Quote.class));
    }

    @Test
    void updateQuoteById_Success() {
        long id = 1L;
        String text = "text";
        String updatedText = "textUpdated";
        String createdBy = "emailUpdated@test.com";
        Utils.initValidSecurityContext();

        QuoteRequestDto request = QuoteRequestDto.builder()
            .id(id)
            .text(updatedText)
            .build();

        Quote savedQuote = Quote.builder()
            .id(id)
            .createdBy(createdBy)
            .text(text)
            .build();

        Quote updatedQuote = Quote.builder()
                .id(id)
                .createdBy(createdBy)
                .text(updatedText)
                .build();

        when(quoteRepository.findByIdAndCreatedBy(anyLong(), anyString())).thenReturn(Optional.of(savedQuote));
        when(quoteRepository.save(any(Quote.class))).thenReturn(updatedQuote);

        QuoteResponseDto result = quoteService.updateQuote(request);

        verify(quoteRepository, times(1)).findByIdAndCreatedBy(anyLong(), anyString());
        verify(quoteRepository, times(1)).save(any(Quote.class));
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(updatedText, result.getText());;
    }

    @Test
    void updateQuoteById_NonExistentQuote_ThrowsQuoteNotFoundException() {
        long id = 1L;
        QuoteRequestDto request = QuoteRequestDto.builder().id(id).build();
        Utils.initValidSecurityContext();

        when(quoteRepository.findByIdAndCreatedBy(anyLong(), anyString())).thenReturn(Optional.empty());

        assertThrows(QuoteNotFoundException.class, () -> quoteService.updateQuote(request));
        verify(quoteRepository, times(1)).findByIdAndCreatedBy(anyLong(), anyString());
    }

    @Test
    void deleteQuoteById_Success() {
        long id = 1L;
        Utils.initValidSecurityContext();

        when(quoteRepository.existsByIdAndCreatedBy(anyLong(), any())).thenReturn(true);
        doNothing().when(quoteRepository).deleteById(anyLong());

        quoteService.deleteById(id);

        verify(quoteRepository, times(1)).existsByIdAndCreatedBy(anyLong(), anyString());
        verify(quoteRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteQuoteById_NonExistentQuote_ThrowsQuoteNotFoundException() {
        Utils.initValidSecurityContext();
        when(quoteRepository.existsByIdAndCreatedBy(anyLong(), any())).thenReturn(false);

        assertThrows(QuoteNotFoundException.class, () -> quoteService.deleteById(1L));
        verify(quoteRepository, times(1)).existsByIdAndCreatedBy(anyLong(), anyString());
        verify(quoteRepository, never()).deleteById(anyLong());
    }

    @Test
    void findAll_Success() {
        List<Quote> searchedQuotes = Utils.createQuotesList();
        List<QuoteResponseDto> searchedQuoteResponseDtos = Utils.convertToQuoteResponseDtos(searchedQuotes);
        Utils.initValidSecurityContext();

        when(quoteRepository.findAllByCreatedBy(anyString())).thenReturn(searchedQuotes);

        List<QuoteResponseDto> result = quoteService.findAll();

        verify(quoteRepository, times(1)).findAllByCreatedBy(anyString());
        assertNotNull(result);
        assertEquals(searchedQuotes.size(), result.size());

        QuoteResponseDto q1 = searchedQuoteResponseDtos.get(0);
        QuoteResponseDto r1 = result.get(0);
        assertEquals(q1.getId(), r1.getId());
        assertEquals(q1.getText(), r1.getText());

        QuoteResponseDto q2 = searchedQuoteResponseDtos.get(1);
        QuoteResponseDto r2 = result.get(1);
        assertEquals(q2.getId(), r2.getId());
        assertEquals(q2.getText(), r2.getText());
    }

    @Test
    void findAll_NoQuotes_ReturnEmptyList() {
        Utils.initValidSecurityContext();
        when(quoteRepository.findAllByCreatedBy(anyString())).thenReturn(Collections.emptyList());

        List<QuoteResponseDto> quotes = quoteService.findAll();

        verify(quoteRepository, times(1)).findAllByCreatedBy(anyString());
        assertThat(quotes).isEmpty();
    }

    @Test
    void findAll_Paginated_Success() {
        List<Quote> searchedQuotes = Utils.createQuotesList();
        Utils.initValidSecurityContext();
        int page = 0;
        int pageSize = searchedQuotes.size();
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Quote> pageQuotes = new PageImpl<>(searchedQuotes, pageable, searchedQuotes.size());

        when(quoteRepository.findAllByCreatedBy(any(Pageable.class), anyString())).thenReturn(pageQuotes);

        QuoteResponsePaginationDto result = quoteService.findAll(page, pageSize);

        verify(quoteRepository, times(1)).findAllByCreatedBy(any(Pageable.class), anyString());
        assertNotNull(result);
        assertEquals(searchedQuotes.size(), result.getQuotes().size());
    }

    @Test
    void findAll_Paginated_EmptyResponse_ReturnsEmptyList() {
        Utils.initValidSecurityContext();
        when(quoteRepository.findAllByCreatedBy(any(Pageable.class), anyString())).thenReturn(Page.empty());

        QuoteResponsePaginationDto pagedQuotes = quoteService.findAll(0, 1);

        verify(quoteRepository, times(1)).findAllByCreatedBy(any(Pageable.class), anyString());
        assertThat(pagedQuotes).isNotNull();
        assertThat(pagedQuotes.getQuotes()).isNull();
    }

    @Test
    void findRandomQuote_Success() {
        List<Quote> searchedQuotes = Utils.createQuotesList();
        Quote quote1 = searchedQuotes.get(0);

        when(quoteRepository.findRandomQuote()).thenReturn(quote1);

        QuoteResponseDto result = quoteService.findRandomQuote();

        verify(quoteRepository, times(1)).findRandomQuote();

        assertNotNull(result);
        assertEquals(quote1.getId(), result.getId());
        assertEquals(quote1.getText(), result.getText());
    }

    @Test
    void findRandomQuote_ReturnEmptyObject() {
        when(quoteRepository.findRandomQuote()).thenReturn(Quote.builder().build());

        QuoteResponseDto result = quoteService.findRandomQuote();

        verify(quoteRepository, times(1)).findRandomQuote();

        assertNotNull(result);
    }

    @Test
    void findQuoteDtosHavingText_Success() {
        List<Quote> searchedQuotes = Utils.createQuotesList();
        List<QuoteResponseDto> searchedQuoteResponseDtos = Utils.convertToQuoteResponseDtos(searchedQuotes);
        String searchedString = "test";

        when(quoteRepository.findByTextContaining(anyString())).thenReturn(searchedQuotes);

        List<QuoteResponseDto> result = quoteService.findQuotesHavingText(searchedString);

        verify(quoteRepository, times(1)).findByTextContaining(anyString());
        assertNotNull(result);
        assertEquals(searchedQuoteResponseDtos.size(), result.size());

        QuoteResponseDto q1 = searchedQuoteResponseDtos.get(0);
        QuoteResponseDto r1 = result.get(0);
        assertEquals(q1.getId(), r1.getId());
        assertEquals(q1.getText(), r1.getText());

        QuoteResponseDto q2 = searchedQuoteResponseDtos.get(1);
        QuoteResponseDto r2 = result.get(1);
        assertEquals(q2.getId(), r2.getId());
        assertEquals(q2.getText(), r2.getText());
    }

    @Test
    void findQuoteDtosHavingText_ReturnsEmptyList() {
        List<Quote> searchedQuotes = new ArrayList<>();
        List<QuoteResponseDto> searchedQuoteResponseDtos = Utils.convertToQuoteResponseDtos(searchedQuotes);
        String searchedString = "test";

        when(quoteRepository.findByTextContaining(anyString())).thenReturn(searchedQuotes);

        List<QuoteResponseDto> result = quoteService.findQuotesHavingText(searchedString);

        verify(quoteRepository, times(1)).findByTextContaining(anyString());
        assertNotNull(result);
        assertEquals(searchedQuoteResponseDtos.size(), result.size());
    }

    @Test
    void findQuoteDtosHavingText_Paginated_Success() {
        List<Quote> searchedQuotes = Utils.createQuotesList();
        String searchedString = "test";
        int page = 0;
        int pageSize = searchedQuotes.size();

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Quote> pageQuotes = new PageImpl<>(searchedQuotes, pageable, searchedQuotes.size());
        when(quoteRepository.findByTextContaining(anyString(), any(Pageable.class))).thenReturn(pageQuotes);

        QuoteResponsePaginationDto result = quoteService.findQuotesHavingText(searchedString, page, pageSize);

        verify(quoteRepository, times(1)).findByTextContaining(anyString(), any(Pageable.class));
        assertNotNull(result);
        assertEquals(searchedQuotes.size(), result.getQuotes().size());
    }

    @Test
    void findQuoteDtosHavingText_Paginated_ReturnsEmptyPage() {
        String searchedString = "test";
        int page = 0;
        int pageSize = 1;

        when(quoteRepository.findByTextContaining(anyString(), any(Pageable.class))).thenReturn(Page.empty());

        QuoteResponsePaginationDto result = quoteService.findQuotesHavingText(searchedString, page, pageSize);

        verify(quoteRepository, times(1)).findByTextContaining(anyString(), any(Pageable.class));
        assertNotNull(result);
        assertNull(result.getQuotes());
    }

//    private void initSecurityContext() {
//        mockSecurityContext = SecurityContextHolder.createEmptyContext();
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userStub, null, userStub.getAuthorities());
//        mockSecurityContext.setAuthentication(authentication);
//        SecurityContextHolder.setContext(mockSecurityContext);
//    }
}