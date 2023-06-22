package com.example.library.mapperTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.library.dao.LibraryDao;
import com.example.library.mapper.LibraryMapper;

@SpringBootTest
public class libraryMapperTest {
    @Autowired
    private LibraryMapper libraryMapper;

    @Test
    public void getLibraryInfoTest(){
        LibraryDao library = libraryMapper.getLibraryInfo(1);
        System.out.println(library.toString());  
    }
}
