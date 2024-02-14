package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.Utils;
import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.exception.ErrorConstants;
import boot.spring.backend.quotes.exception.QuoteInternalException;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.service.cache.CacheService;
import boot.spring.backend.quotes.service.db.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author George Lykoudis
 * @date 7/1/2023
 */
@ExtendWith(MockitoExtension.class)
class QuoteControllerTest {

    @Mock
    QuoteService service;
    
    @Mock
    CacheService cacheService;

    @InjectMocks
    QuoteController controller;
    MockMvc mvc;

    final String BASE_URL = "/api/v1/quotes";
    final String ERROR_CODE_RESPONSE_KEY = "$.errorCode";

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new QuoteControllerAdvice())
                .build();
    }

    @Test
    void createQuote_ValidRequest_Success() throws Exception {
        long id = 1L;
        String text = "text";
        String author = "author";
        QuoteRequestDto request = QuoteRequestDto.builder()
            .author(author)
            .text(text)
            .build();

        QuoteResponseDto quoteResponseDto = QuoteResponseDto.builder()
            .id(id)
            .author(author)
            .text(text)
            .build();

        when(service.saveQuote(any(QuoteRequestDto.class))).thenReturn(quoteResponseDto);

        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value(text))
                .andExpect(jsonPath("$.author").value(author));
    }

    @Test
    void createQuote_NotValidRequest_EmptyTextField_ThrowsMethodArgumentNotValidException() throws Exception {
        QuoteRequestDto request = QuoteRequestDto.builder()
            .author("author")
            .text("")
            .build();

        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY)
                    .value(ErrorConstants.TYPE_MISMATCH_EXCEPTION_MESSAGE.getCode()));
    }

    @Test
    void createQuote_NotValidRequest_NullTextField_ThrowsMethodArgumentNotValidException() throws Exception {
        QuoteRequestDto request = QuoteRequestDto.builder()
            .author("author")
            .build();

        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY)
                    .value(ErrorConstants.TYPE_MISMATCH_EXCEPTION_MESSAGE.getCode()));

    }

    @Test
    void getQuoteById_ValidRequest_Success() throws Exception {
        long id = 1L;
        String text = "text";
        String author = "author";

        QuoteResponseDto quoteResponseDto = QuoteResponseDto.builder()
            .id(id)
            .author(author)
            .text(text)
            .build();

        when(service.findQuoteById(anyLong())).thenReturn(quoteResponseDto);

        mvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value(text))
                .andExpect(jsonPath("$.author").value(author));
    }

    @Test
    void getQuoteById_ValidRequest_ReturnsNotFound() throws Exception {
        long id = 1L;

        when(service.findQuoteById(anyLong())).thenThrow(new QuoteNotFoundException());

        mvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY)
                    .value(ErrorConstants.QUOTE_NOT_FOUND.getCode()));
    }

    @Test
    void getQuoteById_NotValidRequest_ReturnsBadRequest() throws Exception {
        long id = 1L;

        when(service.findQuoteById(anyLong())).thenThrow(MethodArgumentTypeMismatchException.class);

        mvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY)
                    .value(ErrorConstants.TYPE_MISMATCH_EXCEPTION_MESSAGE.getCode()));
    }

    @Test
    void updateQuote_ValidRequest_Success() throws Exception {
        long id = 1L;
        String text = "text";
        String author = "author";

        QuoteRequestDto request = QuoteRequestDto.builder()
            .id(id)
            .author(author)
            .text(text)
            .build();

        String updatedText = "text2";
        QuoteResponseDto quoteResponseDto = QuoteResponseDto.builder()
            .id(id)
            .author(author)
            .text(updatedText)
            .build();

        when(service.updateQuote(any(QuoteRequestDto.class))).thenReturn(quoteResponseDto);

        mvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value(updatedText))
                .andExpect(jsonPath("$.author").value(author));
    }

    @Test
    void updateQuoteById_ValidRequest_NonExistentQuote_ThrowsQuoteNotFoundException() throws Exception {
        long id = 1L;
        String text = "text";
        String author = "author";

        QuoteRequestDto request = QuoteRequestDto.builder()
            .id(id)
            .author(author)
            .text(text)
            .build();

        when(service.updateQuote(any(QuoteRequestDto.class))).thenThrow(new QuoteNotFoundException());

        mvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY)
                    .value(ErrorConstants.QUOTE_NOT_FOUND.getCode()));
    }

    @Test
    void deleteQuoteById_ValidRequest_Success() throws Exception {
        long id = 1L;

        doNothing().when(service).deleteById(anyLong());

        mvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteQuoteById_ValidRequest_NonExistentQuote_ThrowsQuoteNotFoundException() throws Exception {
        long id = 1L;

        doThrow(new QuoteNotFoundException()).when(service).deleteById(anyLong());

        mvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY)
                    .value(ErrorConstants.QUOTE_NOT_FOUND.getCode()));
    }

    @Test
    void findQuotes_Success() throws Exception {
        List<QuoteResponseDto> quoteResponseDtos = Utils.convertToQuoteResponseDtos();

        when(service.findAll()).thenReturn(quoteResponseDtos);

        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk());
    }

    @Test
    void findQuotes_EmptyTable_ReturnsEmptyList() throws Exception {
        List<QuoteResponseDto> quoteResponse = new ArrayList<>();

        when(service.findAll()).thenReturn(quoteResponse);

        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void findRandomQuote_Success() throws Exception {
        long id = 1L;
        String text = "test";
        String author = "author";

        QuoteResponseDto quote = QuoteResponseDto.builder()
            .id(id)
            .author(author)
            .text(text)
            .build();

        when(service.findRandomQuote()).thenReturn(quote);

        mvc.perform(get(BASE_URL + "/random"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value(text))
                .andExpect(jsonPath("$.author").value(author));
    }

    @Test
    void findRandomQuote_EmptyTable_ThrowsQuoteTableEmptyException() throws Exception {

        when(service.findRandomQuote()).thenThrow(new QuoteNotFoundException());

        mvc.perform(get(BASE_URL + "/random"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY)
                    .value(ErrorConstants.QUOTE_NOT_FOUND.getCode()));
    }

    @Test
    void findRandomQuote_CouldNotFindQuote_ThrowsQuoteNotFoundException() throws Exception {
        when(service.findRandomQuote()).thenThrow(new QuoteNotFoundException());

        mvc.perform(get(BASE_URL + "/random"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY)
                    .value(ErrorConstants.QUOTE_NOT_FOUND.getCode()));
    }

    @Test
    void findQuotesHavingText_ValidRequest_Success() throws Exception {
        List<QuoteResponseDto> quotes = Utils.convertToQuoteResponseDtos();
        String searchString = "searchFor";

        when(service.findQuotesHavingText(anyString())).thenReturn(quotes);

        mvc.perform(get(BASE_URL + "/search")
                        .param("t", searchString))
                .andExpect(status().isOk());
    }

    @Test
    void findQuotesHavingText_ValidRequest_EmptyList() throws Exception {
        List<QuoteResponseDto> quotes = new ArrayList<>();
        String searchString = "searchFor";

        when(service.findQuotesHavingText(anyString())).thenReturn(quotes);

        mvc.perform(get(BASE_URL + "/search")
                        .param("t", searchString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void clearCache_ValidResponse() throws Exception {
        doNothing().when(cacheService).clear();

        mvc.perform(post(BASE_URL + "/refresh"))
            .andExpect(status().isNoContent());
    }

    @Test
    void clearCache_InternalError() throws Exception {
        doThrow(new QuoteInternalException()).when(cacheService).clear();

        mvc.perform(post(BASE_URL + "/refresh"))
            .andExpect(status().isInternalServerError());
    }

}