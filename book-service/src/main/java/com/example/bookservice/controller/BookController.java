package com.example.bookservice.controller;

import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            return ResponseEntity.badRequest().build();
        }
        Book savedBook = bookRepository.save(book);
        return ResponseEntity
                .created(URI.create("/api/books/" + savedBook.getId()))
                .body(savedBook);
    }

    @GetMapping("/available")
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailable(true);
    }
}
