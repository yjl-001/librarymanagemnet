package com.example.library.dao;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryDao {
    private Integer id;
    private String libraryName;
    private String libraryMessage;
    private String openTime;
    private String closeTime;
    private List<BookDao> collection;
}
