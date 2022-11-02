package com.ameycode.onlinebookstoreservice.controller;

import com.ameycode.onlinebookstoreservice.dto.BookRequest;
import com.ameycode.onlinebookstoreservice.dto.BookResponse;
import com.ameycode.onlinebookstoreservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    @PostMapping()
    public ResponseEntity<String> addBook(@RequestBody BookRequest bookRequest){
        bookService.addBook(bookRequest);
        return new ResponseEntity<String>("Book Added Successfully!",HttpStatus.CREATED);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse getBookById(@PathVariable("id") long id){
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@RequestBody BookRequest bookRequest, @PathVariable("id") long id) {
        bookService.updateBook(id,bookRequest);
        return new ResponseEntity<String>("Book Id: "+id+" Updated Successfully!",HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") long id) {
        bookService.deleteBookById(id);
        return new ResponseEntity<String>("Book Id: "+id+" Deleted Successfully!",HttpStatus.OK);
    }

    @GetMapping("/checkout")
    public int getTotalPrice(@RequestParam("bookIds") List<Long> bookIds, @RequestParam(value = "promoCode", required = false) String promoCode) {
        return bookService.getTotalPrice(bookIds, promoCode);
    }

}
