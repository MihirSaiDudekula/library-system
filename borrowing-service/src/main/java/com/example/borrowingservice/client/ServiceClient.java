package com.example.borrowingservice.client;

import com.example.borrowingservice.dto.BookDTO;
import com.example.borrowingservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

@Component
public class ServiceClient {
    private static final String BOOK_SERVICE = "book-service";
    private static final String USER_SERVICE = "user-service";
    
    private final WebClient webClient;

    @Autowired
    public ServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    private static final int TIMEOUT_SECONDS = 5;

    public BookDTO getBookDetails(Long bookId) {
        return getBookDetailsAsync(bookId)
                .timeout(java.time.Duration.ofSeconds(TIMEOUT_SECONDS))
                .onErrorResume(e -> Mono.error(new ServiceUnavailableException("Book service is currently unavailable")))
                .block();
    }

    public UserDTO getUserDetails(Long userId) {
        return getUserDetailsAsync(userId)
                .timeout(java.time.Duration.ofSeconds(TIMEOUT_SECONDS))
                .onErrorResume(e -> Mono.error(new ServiceUnavailableException("User service is currently unavailable")))
                .block();
    }
    
    public Mono<BookDTO> getBookDetailsAsync(Long bookId) {
        return webClient.get()
                .uri("http://" + BOOK_SERVICE + "/api/books/{id}", bookId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> 
                    Mono.error(new ResourceNotFoundException("Book not found with id: " + bookId)))
                .onStatus(HttpStatus::is5xxServerError, response -> 
                    Mono.error(new ServiceUnavailableException("Book service is currently unavailable")))
                .bodyToMono(BookDTO.class);
    }
    
    public Mono<UserDTO> getUserDetailsAsync(Long userId) {
        return webClient.get()
                .uri("http://" + USER_SERVICE + "/api/users/{id}", userId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> 
                    Mono.error(new ResourceNotFoundException("User not found with id: " + userId)))
                .onStatus(HttpStatus::is5xxServerError, response -> 
                    Mono.error(new ServiceUnavailableException("User service is currently unavailable")))
                .bodyToMono(UserDTO.class);
    }
}
