package com.example.springbootcrud.service;

import com.example.springbootcrud.model.*;
import com.example.springbootcrud.model.request.AuthorCreationRequest;
import com.example.springbootcrud.model.request.BookCreationRequest;
import com.example.springbootcrud.model.request.BookLendRequest;
import com.example.springbootcrud.model.request.MemberCreationRequest;
import com.example.springbootcrud.repository.AuthorRepository;
import com.example.springbootcrud.repository.BookRepository;
import com.example.springbootcrud.repository.LendRepository;
import com.example.springbootcrud.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Member createMember(MemberCreationRequest request) {
        Member member = new Member();
        BeanUtils.copyProperties(request, member);

        return memberRepository.save(member);
    }

    public Member updateMember(Long id, MemberCreationRequest request) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(!optionalMember.isPresent()) {
            throw new EntityNotFoundException("Member not present in the database");
        }

        Member member = optionalMember.get();
        member.setLastName(request.getLastName());
        member.setFirstName(request.getFirstName());

        return memberRepository.save(member);
    }

    public Author createAuthor(AuthorCreationRequest request) {
        Author author = new Author();
        BeanUtils.copyProperties(request, author);

        return authorRepository.save(author);
    }

    public List<String> lendABook(BookLendRequest request) {

        Optional<Member> memberForId = memberRepository.findById(request.getMemberId());
        if (!memberForId.isPresent()) {
            throw new EntityNotFoundException("Member not present in the database");
        }

        Member member = memberForId.get();
        if (member.getMemberStatus() != MemberStatus.ACTIVE) {
            throw new RuntimeException("User is not active to proceed a lending.");
        }

        List<String> booksApprovedToBurrow = new ArrayList<>();

        request.getBookIds().forEach(bookId -> {

            Optional<Book> bookForId = bookRepository.findById(bookId);
            if (!bookForId.isPresent()) {
                throw new EntityNotFoundException("Cant find any book under given ID");
            }

            Optional<Lend> burrowedBook = lendRepository.findByBookAndStatus(bookForId.get(), LendStatus.BURROWED);
            if (!burrowedBook.isPresent()) {
                booksApprovedToBurrow.add(bookForId.get().getName());
                Lend lend = new Lend();
                lend.setMember(memberForId.get());
                lend.setBook(bookForId.get());
                lend.setStatus(LendStatus.BURROWED);
                lend.setStartOn(Instant.now());
                lend.setDueOn(Instant.now().plus(30, ChronoUnit.DAYS));
                lendRepository.save(lend);
            }

        });
        return booksApprovedToBurrow;
    }

}
