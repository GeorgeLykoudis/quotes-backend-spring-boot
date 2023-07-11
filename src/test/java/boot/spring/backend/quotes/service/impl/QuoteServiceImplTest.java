package boot.spring.backend.quotes.service.impl;

import boot.spring.backend.quotes.Utils;
import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.dto.QuoteResponsePaginationDto;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.model.QuoteEntity;
import boot.spring.backend.quotes.repository.QuoteRepository;
import boot.spring.backend.quotes.service.QuoteCacheService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Mock
    QuoteCacheService cacheService;

    @InjectMocks
    QuoteServiceImpl quoteService;

    Utils utils = new Utils();

    @Test
    void saveQuote_Success() {
        long id = 1L;
        String text = "text";
        String author = "author";
        QuoteEntity savedQuote = new QuoteEntity();
        savedQuote.setId(id);
        savedQuote.setText(text);
        savedQuote.setAuthor(author);

        QuoteRequestDto request = new QuoteRequestDto();
        request.setAuthor(author);
        request.setText(text);

        when(quoteRepository.save(any(QuoteEntity.class))).thenReturn(savedQuote);
        QuoteResponseDto result = quoteService.saveQuote(request);

        verify(quoteRepository, times(1)).save(any(QuoteEntity.class));
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(request.getAuthor(), result.getAuthor());
        assertEquals(request.getText(), result.getText());
    }

    @Test
    void findQuoteById_Success() {
        Long id = 1L;
        String text = "text";
        String author = "author";
        QuoteEntity searchedQuote = new QuoteEntity();
        searchedQuote.setId(id);
        searchedQuote.setText(text);
        searchedQuote.setAuthor(author);

        when(cacheService.getQuoteById(anyLong())).thenReturn(searchedQuote);
        QuoteEntity result = cacheService.getQuoteById(id);

        verify(cacheService, times(1)).getQuoteById(anyLong());

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(text, result.getText());
        assertEquals(author, result.getAuthor());
    }

    @Test
    void findQuoteById_NonExistentQuote_ThrowsQuoteNotFoundException() {
        Long id = 1L;

        when(cacheService.getQuoteById(anyLong())).thenThrow(QuoteNotFoundException.class);

        assertThrows(QuoteNotFoundException.class, () -> {
            QuoteEntity result = cacheService.getQuoteById(id);
            verify(cacheService, times(1)).getQuoteById(anyLong());
            assertNull(result);
        });
    }

    @Test
    void updateQuoteById_Success() {
        long id = 1L;
        String text = "text";
        String author = "author";
        String updatedText = "textUpdated";
        String updatedAuthor = "authorUpdated";

        QuoteRequestDto request = new QuoteRequestDto();
        request.setAuthor(updatedText);
        request.setText(updatedText);

        QuoteEntity searchedQuote = new QuoteEntity();
        searchedQuote.setId(id);
        searchedQuote.setText(text);
        searchedQuote.setAuthor(author);

        QuoteEntity savedQuote = new QuoteEntity();
        savedQuote.setId(id);
        savedQuote.setText(updatedText);
        savedQuote.setAuthor(updatedAuthor);

        when(cacheService.getQuoteById(anyLong())).thenReturn(searchedQuote);
        when(quoteRepository.save(any(QuoteEntity.class))).thenReturn(savedQuote);

        QuoteResponseDto result = quoteService.updateQuoteById(id, request);

        verify(cacheService, times(1)).getQuoteById(anyLong());
        verify(quoteRepository, times(1)).save(any(QuoteEntity.class));
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(updatedText, result.getText());
        assertEquals(updatedAuthor, result.getAuthor());
    }

    @Test
    void updateQuoteById_NonExistentQuote_ThrowsQuoteNotFoundException() {
        long id = 1L;

        when(cacheService.getQuoteById(anyLong())).thenThrow(QuoteNotFoundException.class);

        assertThrows(QuoteNotFoundException.class, () -> {
            QuoteResponseDto result = quoteService.updateQuoteById(id, new QuoteRequestDto());
            verify(cacheService, times(1)).getQuoteById(anyLong());
            assertNull(result);
        });
    }

    @Test
    void deleteQuoteById_Success() {
        long id = 1L;

        when(quoteRepository.existsById(anyLong())).thenReturn(true);
        when(cacheService.getQuoteById(anyLong())).thenThrow(QuoteNotFoundException.class);

        quoteService.deleteById(id);

        verify(quoteRepository, times(1)).existsById(anyLong());
        verify(quoteRepository, times(1)).deleteById(anyLong());
        assertThrows(QuoteNotFoundException.class, () -> {
           QuoteResponseDto quote = quoteService.findQuoteById(anyLong());
            verify(cacheService, times(1)).getQuoteById(anyLong());
            assertNull(quote);
        });

    }

    @Test
    void deleteQuoteById_NonExistentQuote_ThrowsQuoteNotFoundException() {
        when(quoteRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(QuoteNotFoundException.class, () -> {
            quoteService.deleteById(anyLong());

            verify(quoteRepository, times(1)).existsById(anyLong());
            verify(quoteRepository, times(0)).deleteById(anyLong());
        });
    }

    @Test
    void findAll_Success() {
        List<QuoteEntity> searchedQuotes = utils.createQuotesList();
        List<QuoteResponseDto> searchedQuoteResponseDtos = utils.convertToQuoteResponseDtos(searchedQuotes);

        when(cacheService.findAll()).thenReturn(searchedQuotes);

        List<QuoteResponseDto> result = quoteService.findAll();

        verify(cacheService, times(1)).findAll();
        assertNotNull(result);
        assertEquals(searchedQuotes.size(), result.size());

        QuoteResponseDto q1 = searchedQuoteResponseDtos.get(0);
        QuoteResponseDto r1 = result.get(0);
        assertEquals(q1.getId(), r1.getId());
        assertEquals(q1.getText(), r1.getText());
        assertEquals(q1.getAuthor(), r1.getAuthor());

        QuoteResponseDto q2 = searchedQuoteResponseDtos.get(1);
        QuoteResponseDto r2 = result.get(1);
        assertEquals(q2.getId(), r2.getId());
        assertEquals(q2.getText(), r2.getText());
        assertEquals(q2.getAuthor(), r2.getAuthor());
    }

    @Test
    void findAll_EmptyTable_ThrowsQuoteNotFoundException() {
        when(cacheService.findAll()).thenThrow(QuoteNotFoundException.class);

        assertThrows(QuoteNotFoundException.class, () -> {
            List<QuoteResponseDto> quotes = quoteService.findAll();
            verify(cacheService.findAll(), times(1));
            assertNull(quotes);
        });
    }

    @Test
    void findAll_Paginated_Success() {
        List<QuoteEntity> searchedQuotes = utils.createQuotesList();

        int page = 0;
        int pageSize = searchedQuotes.size();
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuoteEntity> pageQuotes = new PageImpl<>(searchedQuotes, pageable, searchedQuotes.size());

        when(cacheService.findAll(any(Pageable.class))).thenReturn(pageQuotes);

        QuoteResponsePaginationDto result = quoteService.findAll(page, pageSize);

        verify(cacheService, times(1)).findAll(any(Pageable.class));
        assertNotNull(result);
        assertEquals(searchedQuotes.size(), result.getQuotes().size());
    }

    @Test
    void findAll_Paginated_EmptyResponse_ThrowsQuoteNotFoundException() {
        when(cacheService.findAll(any(Pageable.class))).thenThrow(QuoteNotFoundException.class);

        assertThrows(QuoteNotFoundException.class, () -> {
            QuoteResponsePaginationDto result = quoteService.findAll(0, 1);
            verify(cacheService, times(1)).findAll(any(Pageable.class));
            assertNull(result);
        });
    }

    @Test
    void findRandomQuote_Success() {
        List<QuoteEntity> searchedQuotes = utils.createQuotesList();
        List<Long> ids = searchedQuotes.stream().map(QuoteEntity::getId).collect(Collectors.toList());
        QuoteEntity quote1 = searchedQuotes.get(0);

        when(cacheService.getLimitedQuoteIds()).thenReturn(ids);
        when(cacheService.getQuoteById(anyLong())).thenReturn(quote1);

        QuoteResponseDto result = quoteService.findRandomQuote();

        verify(cacheService, times(1)).getLimitedQuoteIds();
        verify(cacheService, times(1)).getQuoteById(anyLong());
        assertNotNull(result);
        assertEquals(quote1.getId(), result.getId());
        assertEquals(quote1.getText(), result.getText());
        assertEquals(quote1.getAuthor(), result.getAuthor());
    }

    @Test
    @Disabled
    void findRandomQuote_EmptyResponse_ThrowsQuoteTableEmptyException() {
        when(cacheService.getLimitedQuoteIds()).thenThrow(QuoteNotFoundException.class);

        assertThrows(QuoteNotFoundException.class, () -> {
            QuoteResponseDto result = quoteService.findRandomQuote();
            verify(cacheService, times(1)).getLimitedQuoteIds();
            assertNotNull(result);
        });
    }

    @Test
    void findRandomQuote_EmptyResponse_ThrowsQuoteNotFoundException() {
        List<QuoteEntity> searchedQuotes = utils.createQuotesList();
        List<Long> ids = searchedQuotes.stream().map(QuoteEntity::getId).collect(Collectors.toList());

        when(cacheService.getLimitedQuoteIds()).thenReturn(ids);
        when(cacheService.getQuoteById(anyLong())).thenThrow(QuoteNotFoundException.class);

        assertThrows(QuoteNotFoundException.class, () -> {
            QuoteResponseDto result = quoteService.findRandomQuote();
            verify(cacheService, times(1)).getLimitedQuoteIds();
            verify(cacheService, times(1)).getQuoteById(anyLong());
            assertNotNull(result);
        });
    }

    @Test
    void findQuoteDtosHavingText_Success() {
        List<QuoteEntity> searchedQuotes = utils.createQuotesList();
        List<QuoteResponseDto> searchedQuoteResponseDtos = utils.convertToQuoteResponseDtos(searchedQuotes);
        String searchedString = "test";

        when(cacheService.findQuotesHavingText(anyString())).thenReturn(searchedQuotes);

        List<QuoteResponseDto> result = quoteService.findQuotesHavingText(searchedString);

        verify(cacheService, times(1)).findQuotesHavingText(anyString());
        assertNotNull(result);
        assertEquals(searchedQuoteResponseDtos.size(), result.size());

        QuoteResponseDto q1 = searchedQuoteResponseDtos.get(0);
        QuoteResponseDto r1 = result.get(0);
        assertEquals(q1.getId(), r1.getId());
        assertEquals(q1.getText(), r1.getText());
        assertEquals(q1.getAuthor(), r1.getAuthor());

        QuoteResponseDto q2 = searchedQuoteResponseDtos.get(1);
        QuoteResponseDto r2 = result.get(1);
        assertEquals(q2.getId(), r2.getId());
        assertEquals(q2.getText(), r2.getText());
        assertEquals(q2.getAuthor(), r2.getAuthor());
    }

    @Test
    void findQuoteDtosHavingText_EmptyResponse_ThrowsQuoteNotFoundException() {
        String searchedString = "test";

        when(cacheService.findQuotesHavingText(anyString())).thenThrow(QuoteNotFoundException.class);

        assertThrows(QuoteNotFoundException.class, () -> {
            List<QuoteResponseDto> result = quoteService.findQuotesHavingText(searchedString);
            verify(cacheService, times(1)).findQuotesHavingText(anyString());
            assertNull(result);
        });
    }

    @Test
    void findQuoteDtosHavingText_Paginated_Success() {
        List<QuoteEntity> searchedQuotes = utils.createQuotesList();
        String searchedString = "test";
        int page = 0;
        int pageSize = searchedQuotes.size();

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuoteEntity> pageQuotes = new PageImpl<>(searchedQuotes, pageable, searchedQuotes.size());
        when(cacheService.findQuotesHavingText(anyString(), any(Pageable.class))).thenReturn(pageQuotes);

        QuoteResponsePaginationDto result = quoteService.findQuotesHavingText(searchedString, page, pageSize);

        verify(cacheService, times(1)).findQuotesHavingText(anyString(), any(Pageable.class));
        assertNotNull(result);
        assertEquals(searchedQuotes.size(), result.getQuotes().size());
    }

    @Test
    void findQuoteDtosHavingText_Paginated_EmptyResponse_ThrowsQuoteNotFoundException() {
        String searchedString = "test";

        when(cacheService.findQuotesHavingText(anyString(), any(Pageable.class))).thenThrow(QuoteNotFoundException.class);

        assertThrows(QuoteNotFoundException.class, () -> {
            QuoteResponsePaginationDto result = quoteService.findQuotesHavingText(searchedString, 0, 1);
            verify(cacheService, times(1)).findQuotesHavingText(anyString(), any(Pageable.class));
            assertNull(result);
        });
    }
}