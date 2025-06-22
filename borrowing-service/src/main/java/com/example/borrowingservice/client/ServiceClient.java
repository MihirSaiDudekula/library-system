package com.example.borrowingservice.client;

import com.example.borrowingservice.dto.BookDTO;
import com.example.borrowingservice.dto.UserDTO;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ServiceClient {
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;

    public ServiceClient(DiscoveryClient discoveryClient, RestTemplate restTemplate) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    public BookDTO getBookDetails(Long bookId) {
        String url = getServiceUrl("book-service") + "/api/books/" + bookId;
        ResponseEntity<BookDTO> response = restTemplate.getForEntity(url, BookDTO.class);
        return response.getBody();
    }

    public UserDTO getUserDetails(Long userId) {
        String url = getServiceUrl("user-service") + "/api/users/" + userId;
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(url, UserDTO.class);
        return response.getBody();
    }

    private String getServiceUrl(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances.isEmpty()) {
            throw new RuntimeException(serviceName + " service not found in Eureka");
        }
        return instances.get(0).getUri().toString();
    }
}
