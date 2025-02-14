package com.bookstorage.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;
}
