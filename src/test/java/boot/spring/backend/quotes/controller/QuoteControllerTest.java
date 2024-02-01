package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.Utils;
import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.exception.ErrorConstants;
import boot.spring.backend.quotes.exception.QuoteExceptionHandler;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.service.impl.QuoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author George Lykoudis
 * @date 7/1/2023
 */
@ExtendWith(MockitoExtension.class)
class QuoteControllerTest {
    MockMvc mvc;

    @Mock
    QuoteServiceImpl service;

    @InjectMocks
    QuoteController controller;
    Utils utils = new Utils();

    String BASE_URL = "/api/v1/quotes";
    String ERROR_CODE_RESPONSE_KEY = "$.errorCode";

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new QuoteExceptionHandler())
                .build();
    }

    @Test
    void createQuote_ValidRequest_Success() throws Exception {
        long id = 1L;
        String text = "text";
        String author = "author";

        QuoteRequestDto request = new QuoteRequestDto();
        request.setText(text);
        request.setAuthor(author);

        QuoteResponseDto quote = new QuoteResponseDto();
        quote.setId(id);
        quote.setText(text);
        quote.setAuthor(author);

        when(service.saveQuote(any(QuoteRequestDto.class))).thenReturn(quote);

        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utils.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value(text))
                .andExpect(jsonPath("$.author").value(author))
                .andReturn()
                .getResponse();
    }

    @Test
    void createQuote_NotValidRequest_EmptyTextField_ThrowsMethodArgumentNotValidException() throws Exception {
        String text = "";
        String author = "author";
        QuoteRequestDto request = new QuoteRequestDto();
        request.setText(text);
        request.setAuthor(author);

        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utils.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY).value(ErrorConstants.TYPE_MISMATCH_EXCEPTION_MESSAGE.getCode()))
                .andReturn()
                .getResponse();
    }

    @Test
    void createQuote_NotValidRequest_NullTextField_ThrowsMethodArgumentNotValidException() throws Exception {
        String author = "author";
        QuoteRequestDto request = new QuoteRequestDto();
        request.setAuthor(author);

        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utils.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY).value(ErrorConstants.TYPE_MISMATCH_EXCEPTION_MESSAGE.getCode()))
                .andReturn()
                .getResponse();

    }

    @Test
    void getQuoteById_ValidRequest_Success() throws Exception {
        long id = 1L;
        String text = "text";
        String author = "author";

        QuoteResponseDto quoteResponseDto = new QuoteResponseDto();
        quoteResponseDto.setId(id);
        quoteResponseDto.setText(text);
        quoteResponseDto.setAuthor(author);

        when(service.findQuoteById(anyLong())).thenReturn(quoteResponseDto);

        mvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value(text))
                .andExpect(jsonPath("$.author").value(author))
                .andReturn()
                .getResponse();
    }

    @Test
    void getQuoteById_ValidRequest_ReturnsNotFound() throws Exception {
        long id = 1L;

        when(service.findQuoteById(anyLong())).thenThrow(new QuoteNotFoundException());

        mvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY).value(ErrorConstants.QUOTE_NOT_FOUND.getCode()))
                .andReturn()
                .getResponse();
    }

    @Test
    void getQuoteById_NotValidRequest_ReturnsBadRequest() throws Exception {
        long id = 1L;

        when(service.findQuoteById(anyLong())).thenThrow(MethodArgumentTypeMismatchException.class);

        mvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY).value(ErrorConstants.TYPE_MISMATCH_EXCEPTION_MESSAGE.getCode()))
                .andReturn()
                .getResponse();
    }

    @Test
    void updateQuoteById_ValidRequest_Success() throws Exception {
        long id = 1L;
        String text = "text";
        String author = "author";

        QuoteRequestDto request = new QuoteRequestDto();
        request.setText(text);
        request.setAuthor(author);

        QuoteResponseDto quote = new QuoteResponseDto();
        quote.setId(id);
        quote.setText(text);
        quote.setAuthor(author);

        when(service.updateQuoteById(anyLong(), any(QuoteRequestDto.class))).thenReturn(quote);

        mvc.perform(put(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utils.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value(text))
                .andExpect(jsonPath("$.author").value(author))
                .andReturn()
                .getResponse();
    }

    @Test
    void updateQuoteById_ValidRequest_NonExistentQuote_ThrowsQuoteNotFoundException() throws Exception {
        long id = 1L;
        String text = "text";
        String author = "author";

        QuoteRequestDto request = new QuoteRequestDto();
        request.setText(text);
        request.setAuthor(author);

        when(service.updateQuoteById(anyLong(), any(QuoteRequestDto.class)))
                .thenThrow(new QuoteNotFoundException());

        mvc.perform(put(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utils.toJson(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY).value(ErrorConstants.QUOTE_NOT_FOUND.getCode()))
                .andReturn()
                .getResponse();
    }

    @Test
    void deleteQuoteById_ValidRequest_Success() throws Exception {
        long id = 1L;

        doNothing().when(service).deleteById(anyLong());

        mvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();
    }

    @Test
    void deleteQuoteById_ValidRequest_NonExistentQuote_ThrowsQuoteNotFoundException() throws Exception {
        long id = 1L;

        doThrow(new QuoteNotFoundException()).when(service).deleteById(anyLong());

        mvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY).value(ErrorConstants.QUOTE_NOT_FOUND.getCode()))
                .andReturn()
                .getResponse();
    }

    @Test
    void findQuotes_Success() throws Exception {
        List<QuoteResponseDto> quoteResponseDtos = utils.convertToQuoteResponseDtos();

        when(service.findAll()).thenReturn(quoteResponseDtos);

        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
    }

    @Test
    @Disabled
    void findQuotes_EmptyTable_ThrowsQuoteTableEmptyException() throws Exception {
        List<QuoteResponseDto> quoteResponse = new ArrayList<>();

        when(service.findAll()).thenReturn(quoteResponse);

        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY).value(ErrorConstants.QUOTE_NOT_FOUND.getCode())) // TODO
                .andReturn()
                .getResponse();
    }

    @Test
    void findRandomQuote_Success() throws Exception {
        long id = 1L;
        String text = "test";
        String author = "author";

        QuoteResponseDto quote = new QuoteResponseDto();
        quote.setId(id);
        quote.setText(text);
        quote.setAuthor(author);

        when(service.findRandomQuote()).thenReturn(quote);

        mvc.perform(get(BASE_URL + "/random"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value(text))
                .andExpect(jsonPath("$.author").value(author))
                .andReturn()
                .getResponse();
    }

    @Test
    @Disabled
    void findRandomQuote_EmptyTable_ThrowsQuoteTableEmptyException() throws Exception {

        when(service.findRandomQuote()).thenThrow(new QuoteNotFoundException());

        mvc.perform(get(BASE_URL + "/random"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY).value(ErrorConstants.QUOTE_NOT_FOUND.getCode()))
                .andReturn()
                .getResponse();
    }

    @Test
    void findRandomQuote_CouldNotFindQuote_ThrowsQuoteNotFoundException() throws Exception {
        long id = 1L;

        when(service.findRandomQuote()).thenThrow(new QuoteNotFoundException());

        mvc.perform(get(BASE_URL + "/random"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE_RESPONSE_KEY).value(ErrorConstants.QUOTE_NOT_FOUND.getCode()))
                .andReturn()
                .getResponse();
    }

    @Test
    void findQuotesHavingText_ValidRequest_Success() throws Exception {
        List<QuoteResponseDto> quotes = utils.convertToQuoteResponseDtos();
        String searchString = "searchFor";

        when(service.findQuotesHavingText(anyString())).thenReturn(quotes);

        mvc.perform(get(BASE_URL + "/search")
                        .param("t", searchString))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
    }

    @Test
    void findQuotesHavingText_ValidRequest_NotFound() throws Exception {
        List<QuoteResponseDto> quotes = new ArrayList<>();
        String searchString = "searchFor";

        when(service.findQuotesHavingText(anyString())).thenReturn(quotes);

        mvc.perform(get(BASE_URL + "/search")
                        .param("t", searchString))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
    }

}