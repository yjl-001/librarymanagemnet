package com.example.library.mapperTest;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.library.dao.BookDao;
import com.example.library.dao.BorrowDao;
import com.example.library.dao.ItemDao;
import com.example.library.mapper.BookMapper;

@SpringBootTest
public class bookMapperTest {
    @Autowired
    private BookMapper bookMapper;

    @Test
    public void getBooksByLibraryIdTest(){
        List<BookDao> books = bookMapper.getBooksByLibraryId(1);
        for(BookDao book: books){
            System.out.println(book.toString());
        }
    }

    @Test
    public void getLibraryBookByIdTest(){
        BookDao book = bookMapper.getLibraryBookById(1, 1);
        System.out.println(book.toString());
    }

    @Test
    public void searchLibraryBookTest(){
        List<BookDao> books = bookMapper.searchLibraryBook(1, "我");
        for (BookDao book : books) {
            System.out.println(book.toString());
        }
    }

    @Test
    public void insertBookBorrowRecordTest(){
        BorrowDao borrowDao = new BorrowDao();
        borrowDao.setLibraryId(1);
        borrowDao.setBookId(1);
        borrowDao.setUserId(1);
        // 获取系统当前时间
        borrowDao.setBorrowTime(LocalDateTime.now());
        // 计算归还时间
        borrowDao.setReturnTime(borrowDao.getBorrowTime().plusDays(30));
        bookMapper.insertBookBorrowRecord(borrowDao);
    }

    @Test
    public void deleteBookBorrowRecordTest(){
        bookMapper.deleteBookBorrowRecord(1, 1, 1);
    }

    @Test
    public void insertBookAppointmentTest(){
        ItemDao item = new ItemDao();
        item.setUserId(1);
        item.setTime(LocalDateTime.now());
        bookMapper.insertBookAppointment(1, 1, item);
    }

    @Test
    public void getBookAppointmentTest(){
        List<ItemDao> items = bookMapper.getBookAppointment(1, 1);
        for(ItemDao item:items){
            System.out.println(item.toString());
        }
    }
}
