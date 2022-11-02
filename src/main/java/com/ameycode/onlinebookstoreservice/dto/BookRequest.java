package com.ameycode.onlinebookstoreservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private String name;
    private String description;
    private String author;
    private String type;
    private Integer price;
    private String isbn;
}
