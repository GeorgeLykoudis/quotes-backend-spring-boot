package boot.spring.backend.quotes;

import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.model.Quote;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author George Lykoudis
 * @date 7/2/2023
 */
public class Utils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ModelMapper modelMapper = new ModelMapper();

    public <T> T readJson(String content, Class<T> clazz) throws Exception {
        return objectMapper.readValue(content, clazz);
    }

    public <T> T readJson(String content, TypeReference<T> typeReference) throws Exception {
        return objectMapper.readValue(content, typeReference);
    }

    public static <T> String toJson(T content) throws Exception {
        return objectMapper.writeValueAsString(content);
    }

    public static List<Quote> createQuotesList() {
        List<Quote> quotes = new ArrayList<>();
        Quote q1 = Quote.builder()
            .id(1L)
            .text("text 1")
            .author("author 1")
            .build();
        quotes.add(q1);

        Quote q2 = Quote.builder()
            .id(2L)
            .text("text 2")
            .author("author 2")
            .build();
        quotes.add(q2);

        return quotes;
    }

    public static List<QuoteResponseDto> convertToQuoteResponseDtos() {
        return createQuotesList().stream()
                .map(quote -> modelMapper.map(quote, QuoteResponseDto.class))
                .toList();
    }

    public static List<QuoteResponseDto> convertToQuoteResponseDtos(List<Quote> quotes) {
        return quotes.stream()
                .map(quote -> modelMapper.map(quote, QuoteResponseDto.class))
                .toList();
    }
}
