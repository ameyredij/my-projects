package com.ameycode.onlinebookstoreservice.service;

import com.ameycode.onlinebookstoreservice.dto.BookRequest;
import com.ameycode.onlinebookstoreservice.dto.BookResponse;

import java.util.List;

public interface BookService {

    void addBook(BookRequest bookRequest);

    void updateBook(Long id, BookRequest bookRequest);

    List<BookResponse> getAllBooks();

    BookResponse getBookById(Long id);

    BookResponse getBookByIsbn(String isbn);

    void deleteBookById(Long id);

    Integer getTotalPrice(List<Long> books, String promotionCode);
}
