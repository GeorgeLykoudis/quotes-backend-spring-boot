package boot.spring.backend.quotes;

import boot.spring.backend.quotes.dto.quotes.QuoteResponseDto;
import boot.spring.backend.quotes.model.Quote;
import boot.spring.backend.quotes.model.User;
import boot.spring.backend.quotes.model.security.Role;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author George Lykoudis
 * @date 7/2/2023
 */
public class Utils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ModelMapper modelMapper = new ModelMapper();
    private static SecurityContext mockSecurityContext;
    private static SecurityContextHolder securityContextHolder;
    public static final User userStub = User
        .builder()
        .id(1L)
        .email("user")
        .password("password")
        .role(Role.USER)
        .build();

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
            .createdBy("user@test.com")
            .build();
        quotes.add(q1);

        Quote q2 = Quote.builder()
            .id(2L)
            .text("text 2")
            .createdBy("admin@test.com")
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

    public static void initValidSecurityContext() {
        mockSecurityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(userStub, null, userStub.getAuthorities());
        mockSecurityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(mockSecurityContext);
    }
}
