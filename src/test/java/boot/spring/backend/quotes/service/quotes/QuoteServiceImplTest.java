package boot.spring.backend.quotes.service.quotes;

import boot.spring.backend.quotes.Utils;
import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.dto.QuoteResponsePaginationDto;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.model.QuoteEntity;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    void saveQuote_Success() {
        long id = 1L;
        String text = "text";
        String author = "author";
        QuoteEntity savedQuote = QuoteEntity.builder()
            .id(id)
            .author(author)
            .text(text)
            .build();

        QuoteRequestDto request = QuoteRequestDto.builder()
            .author(author)
            .text(text)
            .build();

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
        QuoteEntity searchedQuote = QuoteEntity.builder()
            .id(id)
            .author(author)
            .text(text)
            .build();

        when(quoteRepository.findById(anyLong())).thenReturn(Optional.of(searchedQuote));
        QuoteResponseDto result = quoteService.findQuoteById(id);

        verify(quoteRepository, times(1)).findById(anyLong());

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(text, result.getText());
        assertEquals(author, result.getAuthor());
    }

    @Test
    void findQuoteById_NonExistentQuote_ThrowsQuoteNotFoundException() {
        Long id = 1L;
        when(quoteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(QuoteNotFoundException.class, () -> {
            QuoteResponseDto result = quoteService.findQuoteById(id);
            verify(quoteRepository, times(1)).findById(anyLong());
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

        QuoteRequestDto request = QuoteRequestDto.builder()
            .id(id)
            .author(author)
            .text(text)
            .build();

        QuoteEntity savedQuote = QuoteEntity.builder()
            .id(id)
            .author(updatedAuthor)
            .text(updatedText)
            .build();

        when(quoteRepository.existsById(anyLong())).thenReturn(true);
        when(quoteRepository.save(any(QuoteEntity.class))).thenReturn(savedQuote);

        QuoteResponseDto result = quoteService.updateQuote(request);

        verify(quoteRepository, times(1)).existsById(anyLong());
        verify(quoteRepository, times(1)).save(any(QuoteEntity.class));
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(updatedText, result.getText());
        assertEquals(updatedAuthor, result.getAuthor());
    }

    @Test
    void updateQuoteById_NonExistentQuote_ThrowsQuoteNotFoundException() {
        long id = 1L;

        when(quoteRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(QuoteNotFoundException.class, () -> {
            QuoteResponseDto result = quoteService.updateQuote(QuoteRequestDto.builder().id(id).build());
            verify(quoteRepository, times(1)).existsById(anyLong());
            assertNull(result);
        });
    }

    @Test
    void deleteQuoteById_Success() {
        long id = 1L;

        when(quoteRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(quoteRepository).deleteById(anyLong());

        quoteService.deleteById(id);

        verify(quoteRepository, times(1)).existsById(anyLong());
        verify(quoteRepository, times(1)).deleteById(anyLong());
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
        List<QuoteEntity> searchedQuotes = Utils.createQuotesList();
        List<QuoteResponseDto> searchedQuoteResponseDtos = Utils.convertToQuoteResponseDtos(searchedQuotes);

        when(quoteRepository.findAll()).thenReturn(searchedQuotes);

        List<QuoteResponseDto> result = quoteService.findAll();

        verify(quoteRepository, times(1)).findAll();
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
        when(quoteRepository.findAll()).thenThrow(QuoteNotFoundException.class);

        assertThrows(QuoteNotFoundException.class, () -> {
            List<QuoteResponseDto> quotes = quoteService.findAll();
            verify(quoteRepository.findAll(), times(1));
            assertNull(quotes);
        });
    }

    @Test
    void findAll_Paginated_Success() {
        List<QuoteEntity> searchedQuotes = Utils.createQuotesList();

        int page = 0;
        int pageSize = searchedQuotes.size();
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuoteEntity> pageQuotes = new PageImpl<>(searchedQuotes, pageable, searchedQuotes.size());

        when(quoteRepository.findAll(any(Pageable.class))).thenReturn(pageQuotes);

        QuoteResponsePaginationDto result = quoteService.findAll(page, pageSize);

        verify(quoteRepository, times(1)).findAll(any(Pageable.class));
        assertNotNull(result);
        assertEquals(searchedQuotes.size(), result.getQuotes().size());
    }

    @Test
    void findAll_Paginated_EmptyResponse_ThrowsQuoteNotFoundException() {
        when(quoteRepository.findAll(any(Pageable.class))).thenThrow(QuoteNotFoundException.class);

        assertThrows(QuoteNotFoundException.class, () -> {
            QuoteResponsePaginationDto result = quoteService.findAll(0, 1);
            verify(quoteRepository, times(1)).findAll(any(Pageable.class));
            assertNull(result);
        });
    }

    @Test
    void findRandomQuote_Success() {
        List<QuoteEntity> searchedQuotes = Utils.createQuotesList();
        QuoteEntity quote1 = searchedQuotes.get(0);

        when(quoteRepository.findRandomQuote()).thenReturn(quote1);

        QuoteResponseDto result = quoteService.findRandomQuote();

        verify(quoteRepository, times(1)).findRandomQuote();

        assertNotNull(result);
        assertEquals(quote1.getId(), result.getId());
        assertEquals(quote1.getText(), result.getText());
        assertEquals(quote1.getAuthor(), result.getAuthor());
    }

    @Test
    void findQuoteDtosHavingText_Success() {
        List<QuoteEntity> searchedQuotes = Utils.createQuotesList();
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
        assertEquals(q1.getAuthor(), r1.getAuthor());

        QuoteResponseDto q2 = searchedQuoteResponseDtos.get(1);
        QuoteResponseDto r2 = result.get(1);
        assertEquals(q2.getId(), r2.getId());
        assertEquals(q2.getText(), r2.getText());
        assertEquals(q2.getAuthor(), r2.getAuthor());
    }

    @Test
    void findQuoteDtosHavingText_Paginated_Success() {
        List<QuoteEntity> searchedQuotes = Utils.createQuotesList();
        String searchedString = "test";
        int page = 0;
        int pageSize = searchedQuotes.size();

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuoteEntity> pageQuotes = new PageImpl<>(searchedQuotes, pageable, searchedQuotes.size());
        when(quoteRepository.findByTextContaining(anyString(), any(Pageable.class))).thenReturn(pageQuotes);

        QuoteResponsePaginationDto result = quoteService.findQuotesHavingText(searchedString, page, pageSize);

        verify(quoteRepository, times(1)).findByTextContaining(anyString(), any(Pageable.class));
        assertNotNull(result);
        assertEquals(searchedQuotes.size(), result.getQuotes().size());
    }
}