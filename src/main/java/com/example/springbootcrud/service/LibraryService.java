package com.example.springbootcrud.service;

import com.example.springbootcrud.model.Author;
import com.example.springbootcrud.model.Book;
import com.example.springbootcrud.model.request.BookCreationRequest;
import com.example.springbootcrud.repository.AuthorRepository;
import com.example.springbootcrud.repository.BookRepository;
import com.example.springbootcrud.repository.LendRepository;
import com.example.springbootcrud.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final AuthorRepository authorRepository;
    private final MemberRepository memberRepository;
    private final LendRepository lendRepository;
    private final BookRepository bookRepository;

    public Book readBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()) {
            return book.get();
        }
        throw new EntityNotFoundException("Can't find any book under given ID");
    }

    public List<Book> readBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book readBook(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if(book.isPresent()) {
            return book.get();
        }
        throw new EntityNotFoundException("Can't find any book under given ISBN");
    }

    public Book createBook(BookCreationRequest book) {
        Optional<Author> author = authorRepository.findById(book.getAuthorId());
        if(!author.isPresent()) {
            throw new EntityNotFoundException("Author Not Found");
        }

        Book bookToCreate = new Book();
        BeanUtils.copyProperties(book, bookToCreate);
        bookToCreate.setAuthor(author.get());

        return bookRepository.save(bookToCreate);
    }



}
