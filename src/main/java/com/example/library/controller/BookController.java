package com.example.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.dao.CommentDao;
import com.example.library.service.BookService;
import com.example.library.utils.ResponseResult;
/**
 * 图书管理模块的接口
 * 接口访问需要有"student"权限
 */
@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    /**
     * 查询图书馆的图书列表
     * @param libraryId
     * @return
     */
    @RequestMapping(value = "/library/books",method=RequestMethod.GET)
    @PreAuthorize("hasAuthority('student')")
    public ResponseResult getLibraryBooks(@RequestParam("libraryId")Integer libraryId){
        return bookService.getLibraryBooks(libraryId);
    }

    /**
     * 查询某本图书的详情
     * @param libraryId
     * @param bookId
     * @return
     */
    @RequestMapping(value = "/library/book",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('student')")
    public ResponseResult getLibraryBookById(@RequestParam("libraryId")Integer libraryId,@RequestParam("bookId")Integer bookId){
        return bookService.getLibraryBookById(libraryId, bookId);
    }
    
    /**
     * 根据图书名进行模糊搜索
     * @param libraryId
     * @param bookName
     * @return
     */
    @RequestMapping(value = "library/book/search",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('student')")
    public ResponseResult searchLibraryBook(@RequestParam("libraryId")Integer libraryId,@RequestParam("bookName")String bookName){
        return bookService.searchLibraryBook(libraryId, bookName);
    }

    /**
     * 添加借阅记录
     * @param libraryId
     * @param bookId
     * @return
     */
    @RequestMapping(value = "/library/book/borrow-record",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('student')")
    public ResponseResult insertBookBorrowRecord(@RequestParam("libraryId") Integer libraryId, @RequestParam("bookId") Integer bookId){
        return bookService.insertBookBorrowRecord(libraryId, bookId);
    }

    /**
     * 删除借阅记录
     * @param libraryId
     * @param bookId
     * @return
     */
    @RequestMapping(value = "/library/book/borrow-record",method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('student')")
    public ResponseResult deleteBookBorrowRecord(@RequestParam("libraryId") Integer libraryId, @RequestParam("bookId") Integer bookId){
        return bookService.deleteBookBorrowRecord(libraryId,bookId);
    }

    /**
     * 添加图书评论
     * @param libraryId
     * @param bookId
     * @param comment
     * @return
     */
    @RequestMapping(value = "/library/book/comment",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('student')")
    public ResponseResult insertBookComment(@RequestParam("libraryId") Integer libraryId,@RequestParam("bookId") Integer bookId,@RequestBody CommentDao comment){
        return bookService.insertBookComment(libraryId,bookId,comment);
    }

    /**
     * 添加预约记录
     * @param libraryId
     * @param bookId
     * @return
     */
    @RequestMapping(value = "/library/book/appointment",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('student')")
    public ResponseResult insertBookAppointment(@RequestParam("libraryId") Integer libraryId,@RequestParam("bookId") Integer bookId){
        return bookService.insertBookAppointment(libraryId,bookId);
    }

    /**
     * 获取某本图书的预约信息
     * @param libraryId
     * @param bookId
     * @return
     */
    @RequestMapping(value = "/library/book/appointment", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('student')")
    public ResponseResult getBookAppointment(@RequestParam("libraryId") Integer libraryId, @RequestParam("bookId") Integer bookId) {
        return bookService.getBookAppointment(libraryId, bookId);
    }

    /**
     * 添加排队记录
     * @param libraryId
     * @param bookId
     * @return
     */
    @RequestMapping(value = "/library/book/queue", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('student')")
    public ResponseResult insertBookQueue(@RequestParam("libraryId") Integer libraryId,
            @RequestParam("bookId") Integer bookId) {
        return bookService.insertBookQueue(libraryId, bookId);
    }

    /**
     * 获取某本图书的排队信息
     * @param libraryId
     * @param bookId
     * @return
     */
    @RequestMapping(value = "/library/book/queue", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('student')")
    public ResponseResult getBookQueue(@RequestParam("libraryId") Integer libraryId,
            @RequestParam("bookId") Integer bookId) {
        return bookService.getBookQueue(libraryId, bookId);
    }

}
