package com.example.borrowingservice.controller;

import com.example.borrowingservice.client.ServiceClient;
import com.example.borrowingservice.dto.BookDTO;
import com.example.borrowingservice.dto.UserDTO;
import com.example.borrowingservice.model.Borrowing;
import com.example.borrowingservice.repository.BorrowingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingRepository borrowingRepository;
    private final ServiceClient serviceClient;

    public BorrowingController(BorrowingRepository borrowingRepository, ServiceClient serviceClient) {
        this.borrowingRepository = borrowingRepository;
        this.serviceClient = serviceClient;
    }

    @PostMapping
    public ResponseEntity<Borrowing> borrowBook(
            @RequestParam Long bookId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "14") int days) {
        
        // In a real app, we would verify book and user exist in their respective services
        
        Borrowing borrowing = new Borrowing(
            bookId,
            userId,
            LocalDate.now(),
            LocalDate.now().plusDays(days)
        );
        
        Borrowing savedBorrowing = borrowingRepository.save(borrowing);
        return ResponseEntity
                .created(URI.create("/api/borrowings/" + savedBorrowing.getId()))
                .body(savedBorrowing);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable Long id) {
        return borrowingRepository.findById(id)
                .map(borrowing -> {
                    borrowing.setReturned(true);
                    borrowingRepository.save(borrowing);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Borrowing> getBorrowings(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId) {
        
        if (userId != null) {
            return borrowingRepository.findByUserId(userId);
        }
        if (bookId != null) {
            return borrowingRepository.findByBookId(bookId);
        }
        return borrowingRepository.findAll();
    }
    
    @GetMapping("/details/{borrowingId}")
    public ResponseEntity<Map<String, Object>> getBorrowingDetails(@PathVariable Long borrowingId) {
        return borrowingRepository.findById(borrowingId)
            .map(borrowing -> {
                // Get book and user details
                BookDTO book = serviceClient.getBookDetails(borrowing.getBookId());
                UserDTO user = serviceClient.getUserDetails(borrowing.getUserId());
                
                // Create response
                Map<String, Object> response = new HashMap<>();
                response.put("borrowing", borrowing);
                response.put("book", book);
                response.put("user", user);
                
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
