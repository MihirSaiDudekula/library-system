package com.example.borrowingservice.repository;

import com.example.borrowingservice.model.Borrowing;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BorrowingRepository extends R2dbcRepository<Borrowing, Long> {
    Flux<Borrowing> findByUserId(Long userId);
    Flux<Borrowing> findByBookId(Long bookId);
    Flux<Borrowing> findByReturned(boolean returned);
}
