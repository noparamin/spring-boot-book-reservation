package com.example.springbootcrud.repository;

import com.example.springbootcrud.model.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>{
    Optional<Book> findByIsbn(String isbn);
}
