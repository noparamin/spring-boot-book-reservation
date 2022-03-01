package com.example.springbootcrud.controller;

import com.example.springbootcrud.model.Author;
import com.example.springbootcrud.model.Book;
import com.example.springbootcrud.model.Member;
import com.example.springbootcrud.model.request.AuthorCreationRequest;
import com.example.springbootcrud.model.request.BookCreationRequest;
import com.example.springbootcrud.model.request.BookLendRequest;
import com.example.springbootcrud.model.request.MemberCreationRequest;
import com.example.springbootcrud.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @GetMapping("/book")
    public ResponseEntity readBooks(@RequestParam(required = false) String isbn) {
        if(isbn == null) {
            return ResponseEntity.ok(libraryService.readBooks());
        }
        return ResponseEntity.ok(libraryService.readBook(isbn));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Book> readBook (@PathVariable Long bookId){
        return ResponseEntity.ok(libraryService.readBook(bookId));
    }

    @PostMapping("/book")
    public ResponseEntity<Book> createBook (@RequestBody BookCreationRequest request) {
        return ResponseEntity.ok(libraryService.createBook(request));
    }

    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        libraryService.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/member")
    public ResponseEntity<Member> createMember(@RequestBody MemberCreationRequest request) {
        return ResponseEntity.ok(libraryService.createMember(request));
    }

    @PatchMapping("/member/{memberId}")
    public ResponseEntity<Member> updateMember(@RequestBody MemberCreationRequest request, @PathVariable Long memberId) {
        return ResponseEntity.ok(libraryService.updateMember(memberId, request));
    }

    @PostMapping("/book/lend")
    public ResponseEntity<List<String>> lendBook(@RequestBody BookLendRequest bookLendRequests) {
        return ResponseEntity.ok(libraryService.lendABook(bookLendRequests));
    }

    @PostMapping("/author")
    public ResponseEntity<Author> createAuthor(@RequestBody AuthorCreationRequest request) {
        return ResponseEntity.ok(libraryService.createAuthor(request));
    }
}
