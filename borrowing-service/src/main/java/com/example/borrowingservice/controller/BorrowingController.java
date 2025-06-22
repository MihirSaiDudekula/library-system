package com.example.borrowingservice.controller;

import com.example.borrowingservice.model.Borrowing;
import com.example.borrowingservice.repository.BorrowingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingRepository borrowingRepository;

    public BorrowingController(BorrowingRepository borrowingRepository) {
        this.borrowingRepository = borrowingRepository;
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
}
