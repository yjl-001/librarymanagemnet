package com.example.library.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.example.library.dao.BookDao;
import com.example.library.dao.BorrowDao;
import com.example.library.dao.CommentDao;
import com.example.library.dao.ItemDao;

import io.lettuce.core.dynamic.annotation.Param;

@Mapper
public interface BookMapper {
    /**
     * 查询图书馆的所有图书
     * @param libraryId
     * @return
     */
    @Select("SELECT book.id,book.bookName,book.bookDescription,book.publicationTime,book.number,author.authorName as author,classfication.className as classfication FROM book,classfication,author WHERE book.authorId=author.id AND book.classId=classfication.id AND book.libraryId=#{libraryId} ORDER BY book.id")
    List<BookDao> getBooksByLibraryId(@Param(value = "libraryId")Integer libraryId);

    /**
     * 查询图书馆下某本图书的信息
     * @param libraryId
     * @param bookId
     * @return
     */
    @Select("SELECT book.id,book.bookName,book.bookDescription,book.publicationTime,book.number,author.authorName as author,classfication.className as classfication FROM book,classfication,author WHERE book.authorId=author.id AND book.classId=classfication.id AND book.libraryId=#{libraryId} AND book.id=#{bookId}")
    BookDao getLibraryBookById(@Param(value = "libraryId")Integer libraryId,@Param(value = "bookId")Integer bookId);

    /**
     * 模糊搜素
     * @param libraryId
     * @param bookName
     * @return
     */
    @Select("SELECT book.id,book.bookName,book.bookDescription,book.publicationTime,book.number,author.authorName as author,classfication.className as classfication FROM book,classfication,author WHERE book.authorId=author.id AND book.classId=classfication.id AND book.libraryId=#{libraryId} AND book.bookName LIKE CONCAT('%',#{bookName},'%')")
    List<BookDao> searchLibraryBook(@Param(value = "libraryId") Integer libraryId, @Param(value = "bookName") String bookName);

    /**
     * 借书
     * @param borrow
     */
    @Insert("INSERT INTO borrow(libraryId,bookId,userId,borrowTime,returnTime) VALUES(#{libraryId},#{bookId},#{userId},#{borrowTime},#{returnTime})")
    void insertBookBorrowRecord(@Param(value = "borrow") BorrowDao borrow);

    /**
     * 还书
     * @param libraryId
     * @param bookId
     * @param userId
     */
    @Delete("DELETE FROM borrow WHERE libraryId=#{libraryId} AND bookId=#{bookId} AND userId=#{userId}")
    void deleteBookBorrowRecord(@Param(value = "libraryId") Integer libraryId,@Param(value = "bookId") Integer bookId,@Param(value = "userId")Integer userId);

    /**
     * 评论
     * @param borrow
     */
    @Insert("INSERT INTO comment(libraryId,bookId,userId,comment,score) VALUES(#{libraryId},#{bookId},#{userId},#{comment},#{score})")
    void insertBookComment(@Param(value = "comment") CommentDao comment);

    /**
     * 预约
     * @param libraryId
     * @param bookId
     * @param userId
     */
    @Insert("INSERT INTO appointment(libraryId,bookId,userId,appointmentTime) VALUES(#{libraryId},#{bookId},#{appointment.userId},#{appointment.time})")
    void insertBookAppointment(@Param(value = "libraryId")Integer libraryId, @Param(value = "bookId")Integer bookId, @Param(value = "appointment") ItemDao appointment);

    /**
     * 查询预约信息
     * @param libraryId
     * @param bookId
     * @return
     */
    @Select("SELECT user.id as userId, user.username,appointment.appointmentTime as time FROM user,appointment WHERE appointment.libraryId=#{libraryId} AND appointment.bookId=#{bookId} AND appointment.userId=user.id ORDER BY appointment.appointmentTime")
    List<ItemDao> getBookAppointment(@Param(value = "libraryId")Integer libraryId, @Param(value = "bookId")Integer bookId);

    /**
     * 排队
     * 
     * @param libraryId
     * @param bookId
     * @param userId
     */
    @Insert("INSERT INTO queue(libraryId,bookId,userId,queueTime) VALUES(#{libraryId},#{bookId},#{queue.userId},#{queue.time})")
    void insertBookQueue(@Param(value = "libraryId") Integer libraryId, @Param(value = "bookId") Integer bookId, @Param(value = "queue") ItemDao queue);

    /**
     * 查询排队信息
     * 
     * @param libraryId
     * @param bookId
     * @return
     */
    @Select("SELECT user.id as userId, user.username,queue.queueTime as time FROM user,queue WHERE queue.libraryId=#{libraryId} AND queue.bookId=#{bookId} AND queue.userId=user.id ORDER BY queue.queueTime")
    List<ItemDao> getBookQueue(@Param(value = "libraryId") Integer libraryId, @Param(value = "bookId") Integer bookId);
}
