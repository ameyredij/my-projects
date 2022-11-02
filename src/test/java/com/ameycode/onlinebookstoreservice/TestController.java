package com.ameycode.onlinebookstoreservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.Optional;


import com.ameycode.onlinebookstoreservice.controller.BookController;
import com.ameycode.onlinebookstoreservice.dto.BookResponse;
import com.ameycode.onlinebookstoreservice.model.Book;
import com.ameycode.onlinebookstoreservice.repository.BookRepository;
import com.ameycode.onlinebookstoreservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;



import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class TestController {
    @MockBean
    private BookService bookService;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    private MockMvc mockMvc;

    private final Integer price = 250;
    @Autowired
    private ObjectMapper objectMapper;

    String CREATE_USER_URL = "/api/book/" + "1";

    @Test
    void shouldAddBook() throws Exception {
        doNothing().when(bookService).addBook(ArgumentMatchers.any());
        String json = objectMapper.writeValueAsString(getBookDTO());
        mockMvc.perform(post("/api/book").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
    }

    @Test
    void shouldReturnBook() throws Exception {
        List<BookResponse> bookResponses = Arrays.asList(getBookDTO());

        Mockito.when(bookService.getAllBooks()).thenReturn(bookResponses);

        mockMvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Java Book")));
    }



    @Test
    void shouldReturnListOfBook() throws Exception {
        List<BookResponse> books = new ArrayList<>(
                Arrays.asList(new BookResponse(1l,"MyBook233","testing demo","Amey2","Tech2",200,"332211"),
                        new BookResponse(2l,"MyBook233","testing demo","Amey2","Tech2",200,"332211"),
                        new BookResponse(3l,"MyBook233","testing demo","Amey2","Tech2",200,"332211")));

        when(bookService.getAllBooks()).thenReturn(books);
        mockMvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(books.size()))
                .andDo(print());
    }




    @Test
    void updateBook() throws Exception {



        doNothing().when(bookService)
                .updateBook(ArgumentMatchers.any(), ArgumentMatchers.any());
        String json = objectMapper.writeValueAsString(getBookDTO());
        mockMvc.perform(put(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

    }


    @Test
    void deleteBook() throws Exception {

        doNothing().when(bookService).deleteBookById(ArgumentMatchers.any());

        mockMvc.perform(delete(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }


    @Test
    public void checkoutBookTest() throws Exception {
        Mockito.when(bookService.getTotalPrice(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(price);

        mockMvc.perform(get("/api/book/checkout").param("bookIds", "1")
                .param("promoCode", "TECHNOLOGY")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

    }


    private BookResponse getBookDTO(){
        return BookResponse.builder()
                .name("Java Book")
                .author("Amey")
                .description("Tech book")
                .isbn("321123")
                .price(220)
                .type("TECHNOLOGY").build();

    }


}