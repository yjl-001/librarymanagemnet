package com.example.library.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDao {
    private Integer libraryId;
    private Integer bookId;
    private Integer userId;
    private String comment;
    private Integer score;
}
