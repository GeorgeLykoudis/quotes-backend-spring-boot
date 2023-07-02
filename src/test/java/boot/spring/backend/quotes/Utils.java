package boot.spring.backend.quotes;

import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.model.QuoteEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author George Lykoudis
 * @date 7/2/2023
 */
public class Utils {

    private ObjectMapper objectMapper = new ObjectMapper();

    public <T> T readJson(String content, Class<T> clazz) throws Exception {
        return objectMapper.readValue(content, clazz);
    }

    public <T> T readJson(String content, TypeReference<T> typeReference) throws Exception {
        return objectMapper.readValue(content, typeReference);
    }

    public <T> String toJson(T content) throws Exception {
        return objectMapper.writeValueAsString(content);
    }

    public List<QuoteEntity> createQuotesList() {
        List<QuoteEntity> quotes = new ArrayList<>();
        QuoteEntity q1 = new QuoteEntity();
        q1.setId(1L);
        q1.setText("text 1");
        q1.setAuthor("author 1");
        quotes.add(q1);

        QuoteEntity q2 = new QuoteEntity();
        q2.setId(2L);
        q2.setText("text 2");
        q2.setAuthor("author 2");
        quotes.add(q2);

        return quotes;
    }

    public List<QuoteResponseDto> convertToQuoteResponseDtos() {
        return createQuotesList().stream()
                .map(QuoteResponseDto::createQuote)
                .collect(Collectors.toList());
    }

    public List<QuoteResponseDto> convertToQuoteResponseDtos(List<QuoteEntity> quotes) {
        return quotes.stream()
                .map(QuoteResponseDto::createQuote)
                .collect(Collectors.toList());
    }
}
