package com.example.borrowingservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long bookId;       // Reference to Book in Book Service
    private Long userId;       // Reference to User in User Service
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean returned = false;

    public Borrowing(Long bookId, Long userId, LocalDate borrowDate, LocalDate returnDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }
}
