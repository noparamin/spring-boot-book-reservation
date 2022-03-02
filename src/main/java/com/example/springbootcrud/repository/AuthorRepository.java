package com.example.springbootcrud.repository;

import com.example.springbootcrud.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long>{
}
