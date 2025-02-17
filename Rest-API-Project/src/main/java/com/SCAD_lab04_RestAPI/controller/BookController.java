package com.SCAD_lab04_RestAPI.controller;

import com.SCAD_lab04_RestAPI.entity.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {

    private Map<Long, Book> books = new HashMap<>();

    @GetMapping
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        if (books.containsKey(book.getBookId())) {
            return ResponseEntity.badRequest().body("Book with this ID already exists.");
        }
        books.put(book.getBookId(), book);
        return ResponseEntity.ok("Book added successfully!");
    }

    @GetMapping("/id/{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable long bookId) {
        Book book = books.get(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/id/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable long bookId) {
        if (!books.containsKey(bookId)) {
            return ResponseEntity.badRequest().body("Book ID not found.");
        }
        books.remove(bookId);
        return ResponseEntity.ok("Book deleted successfully.");
    }

    @PutMapping("/id/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable long bookId, @RequestBody Book book) {
        if (!books.containsKey(bookId)) {
            return ResponseEntity.badRequest().build();
        }
        book.setBookId(bookId);
        books.put(bookId, book);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable long bookId) {
        Book book = books.get(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        if (!book.borrowBook()) {
            return ResponseEntity.badRequest().body("No available copies to borrow.");
        }
        return ResponseEntity.ok("Book borrowed successfully!");
    }

    @PostMapping("/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable long bookId) {
        Book book = books.get(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        book.returnBook();
        return ResponseEntity.ok("Book returned successfully!");
    }
}

