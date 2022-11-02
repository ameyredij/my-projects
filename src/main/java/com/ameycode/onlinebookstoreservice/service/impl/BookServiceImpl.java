package com.ameycode.onlinebookstoreservice.service.impl;


import com.ameycode.onlinebookstoreservice.configurations.BookTypePromoDiscount;
import com.ameycode.onlinebookstoreservice.dto.BookRequest;
import com.ameycode.onlinebookstoreservice.dto.BookResponse;
import com.ameycode.onlinebookstoreservice.exception.BookServiceException;
import com.ameycode.onlinebookstoreservice.model.Book;
import com.ameycode.onlinebookstoreservice.repository.BookRepository;
import com.ameycode.onlinebookstoreservice.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public void addBook(BookRequest bookRequest) {
        log.info("Inside addBook BookServiceImpl");
        Book book = Book.builder()
                .name(bookRequest.getName())
                .description(bookRequest.getDescription())
                .author(bookRequest.getAuthor())
                .type(bookRequest.getType())
                .price(bookRequest.getPrice())
                .isbn(bookRequest.getIsbn())
                .build();
        bookRepository.save(book);
        log.info("Book Id: {} is Added", book.getId());
    }

    @Override
    public void updateBook(Long id, BookRequest bookRequest) {
        log.info("Inside updateBook BookServiceImpl");
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookServiceException("Book id:" + id + " not found."));
        book.setAuthor(bookRequest.getAuthor());
        book.setDescription(bookRequest.getDescription());
        book.setIsbn(bookRequest.getIsbn());
        book.setName(bookRequest.getName());
        book.setPrice(bookRequest.getPrice());
        book.setType(bookRequest.getType());
        bookRepository.save(book);
        log.info("Book Id: {} is updated", book.getId());

    }

    @Override
    public List<BookResponse> getAllBooks() {
        log.info("Inside getAllBooks BookServiceImpl");
        List<Book> books = bookRepository.findAll();
        return books.stream().map(book -> mapToBookResponse(book)).toList();
    }

    @Override
    public BookResponse getBookById(Long id) {
        log.info("Inside getBookById BookServiceImpl");
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookServiceException("Book id:" + id + " not found."));
        return mapToBookResponse(book);
    }

    @Override
    public BookResponse getBookByIsbn(String isbn) {
        log.info("Inside getBookByIsbn BookServiceImpl");
        Book book = bookRepository.findByIsbn(isbn);
        if (null == book) {
            throw new BookServiceException("Book isbn:" + isbn + " not found.");
        } else {
            return mapToBookResponse(book);
        }
    }

    @Override
    public void deleteBookById(Long id) {
        log.info("Inside deleteBookById BookServiceImpl");
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookServiceException("Book id:" + id + " not found."));
        bookRepository.deleteById(book.getId());
        log.info("Book Id: {} is deleted", book.getId());
    }

    @Override
    public Integer getTotalPrice(List<Long> books, String promoCode) {
        log.info("Inside getTotalPrice BookServiceImpl");
        AtomicReference<Integer> totalPrice = new AtomicReference<>(0);
        books.forEach(id -> {
            Optional<Book> bookId = bookRepository.findById(id);
            bookId.ifPresent(book -> {
                AtomicReference<Integer> discountedAmt = new AtomicReference<>(0);
                if (null != promoCode && !promoCode.isEmpty()) {
                    log.info("Book promoCode : {}", promoCode);
                    log.info("Book Type() : {}", book.getType());
                    log.info(" BookTypePromoDiscount : {}", BookTypePromoDiscount.getDiscountPercentage(book.getType()));
                    log.info("-------------------------------------");
                    Integer discountPercentage = BookTypePromoDiscount.getDiscountPercentage(book.getType());
                    if (null != discountPercentage) {
                        discountedAmt.updateAndGet(v -> v + (book.getPrice() * discountPercentage) / 100);
                    }

                    totalPrice.updateAndGet(v -> v + (book.getPrice() - discountedAmt.get()));
                } else {
                    totalPrice.updateAndGet(v -> v + (book.getPrice()));
                }
                log.info("--------------CheckOut Details-----------------------");
                log.info("Book Name : {}", book.getName());
                log.info("Book Price : {}", book.getPrice());
                log.info("Book Type : {}", book.getType());
                log.info("Book Type Promotion Percentage : {}", BookTypePromoDiscount.getDiscountPercentage(book.getType()));
                log.info("Discount Amount : {}", discountedAmt.get());
                log.info("finalPrice after discount : {}", (book.getPrice() - discountedAmt.get()));

            });
        });


        log.info("totalPrice : {}", totalPrice.get());
        return totalPrice.get();
    }

    private BookResponse mapToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .description(book.getDescription())
                .author(book.getAuthor())
                .type(book.getType())
                .price(book.getPrice())
                .isbn(book.getIsbn())
                .build();
    }

}
