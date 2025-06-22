package com.example.borrowingservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Table("borrowings")
public class Borrowing {
    @Id
    private Long id;
    
    private Long bookId;       // Reference to Book in Book Service
    private Long userId;       // Reference to User in User Service
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean returned = false;

    public Borrowing(Long id, Long bookId, Long userId, LocalDate borrowDate, LocalDate returnDate, boolean returned) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }
    
    public Borrowing(Long bookId, Long userId, LocalDate borrowDate, LocalDate returnDate) {
        this(null, bookId, userId, borrowDate, returnDate, false);
    }
}
