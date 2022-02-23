package com.example.springbootcrud.repository;

import com.example.springbootcrud.model.Book;
import com.example.springbootcrud.model.Lend;
import com.example.springbootcrud.model.LendStatus;

import java.util.Optional;

public interface LendRepository extends JpaRepository<Lend, Long>{
    Optional<Lend> findByBookAndStatus(Book book, LendStatus lendStatus);
}
