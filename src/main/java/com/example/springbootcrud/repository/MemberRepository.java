package com.example.springbootcrud.repository;

import com.example.springbootcrud.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
