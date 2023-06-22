package com.example.library.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.dao.BookDao;
import com.example.library.dao.BorrowDao;
import com.example.library.dao.CommentDao;
import com.example.library.dao.ItemDao;
import com.example.library.dao.LoginUser;
import com.example.library.mapper.BookMapper;
import com.example.library.utils.GetLoginUser;
import com.example.library.utils.ResponseResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class BookService {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private GetLoginUser getLoginUser;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    final private int BORROWDAYS = 30;

    /**
     * 查询图书馆下的所有图书
     * @param libraryId
     * @return
     */
    public ResponseResult getLibraryBooks(Integer libraryId) {
        List<BookDao> collection = bookMapper.getBooksByLibraryId(libraryId);
        if (collection.size() > 0) {// 判断图书馆有无藏书
            response.setStatus(200);
            return new ResponseResult<List<BookDao>>(200, "查询成功", collection);
        } else {
            response.setStatus(505);
            return new ResponseResult<>(505, "图书馆不存在或图书馆无藏书", null);
        }
    }

    /**
     * 查询某本图书的详情
     * @param libraryId
     * @param bookId
     * @return
     */
    public ResponseResult getLibraryBookById(Integer libraryId,Integer bookId){
        BookDao book = bookMapper.getLibraryBookById(libraryId, bookId);
        if(book!=null){ // 判断数据库中是否有该图书
            return new ResponseResult<BookDao>(200, "查询成功", book);
        }else{
            response.setStatus(505);
            return new ResponseResult<>(505, "查询失败，图书不存在", null);
        }
    }

    /**
     * 根据图书名进行模糊搜索
     * @param libraryId
     * @param bookName
     * @return
     */
    public ResponseResult searchLibraryBook(Integer libraryId,String bookName){
        List<BookDao> books = bookMapper.searchLibraryBook(libraryId, bookName);
        if(books.size()>0){ // 判断查结果有无包含关键字的图书
            return new ResponseResult<List<BookDao>>(200, "搜索成功", books);
        }
        response.setStatus(505);
        return new ResponseResult<>(505, "搜索无结果", null);
    }

    /**
     * 插入图书借阅记录
     * @param libraryId
     * @param bookId
     * @return
     */
    public ResponseResult insertBookBorrowRecord(Integer libraryId,Integer bookId){
        BookDao book = bookMapper.getLibraryBookById(libraryId, bookId);
        if(book==null){//查询图书是否存在
            response.setStatus(506);
            return new ResponseResult<>(506, "图书不存在", null);
        }
        if(book.getNumber()<=0){// 检验图书是否有库存
            response.setStatus(507);
            return new ResponseResult<>(507, "该图书已无库存,您可以预约", null);
        }
        BorrowDao borrowDao = new BorrowDao();
        borrowDao.setLibraryId(libraryId);
        borrowDao.setBookId(bookId);
        try{
            // 获取用户ID
            LoginUser loginUser = getLoginUser.getLoginUser(request);
            borrowDao.setUserId(loginUser.getUser().getId());
            // 获取系统当前时间
            borrowDao.setBorrowTime(LocalDateTime.now());
            // 计算归还时间
            borrowDao.setReturnTime(borrowDao.getBorrowTime().plusDays(BORROWDAYS));
            try{
            //插入数据
            bookMapper.insertBookBorrowRecord(borrowDao);
            Map<String, String> data = new HashMap<>();
            data.put("returnTime",
            borrowDao.getReturnTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return new ResponseResult<Map<String, String>>(200, "借阅成功，请在规定时间内返回", data);
            }catch(Exception e){// 捕获SQL异常
                response.setStatus(509);
                return new ResponseResult<>(509, "数据库插入失败", null);
            }
        }catch(RuntimeException e){ // 捕获getLoginUser抛出的异常
            response.setStatus(403);
            return new ResponseResult<>(403, "请先登录", null);
        }
    }

    /**
     * 删除借阅记录
     * @param libraryId
     * @param bookId
     * @return
     */
    public ResponseResult deleteBookBorrowRecord(Integer libraryId, Integer bookId){
        try{
            // 从redis中获取登录用户
            LoginUser loginUser = getLoginUser.getLoginUser(request);
            try{
                bookMapper.deleteBookBorrowRecord(libraryId,bookId,loginUser.getUser().getId());
                return new ResponseResult<>(200, "归还成功", null);
            }catch(Exception e){// 捕获SQL执行异常
                response.setStatus(506);
                return new ResponseResult<>(506, "数据库删除失败", null);
            }
        }catch (RuntimeException e) {// 捕获getLoginUser抛出的异常
            response.setStatus(403);
            return new ResponseResult<>(403, "请先登录", null);
        }
    }

    /**
     * 插入图书评论
     * @param libraryId
     * @param bookId
     * @param comment
     * @return
     */
    public ResponseResult insertBookComment(Integer libraryId,Integer bookId,CommentDao comment){
        if(comment == null){ // 参数检验
            response.setStatus(508);
            return new ResponseResult<>(508, "数据为空", null);
        }
        comment.setLibraryId(libraryId);
        comment.setBookId(bookId);
        try{
            // 从redis中获取登录用户
            LoginUser loginUser = getLoginUser.getLoginUser(request);
            comment.setUserId(loginUser.getUser().getId());
            try{
                bookMapper.insertBookComment(comment);
                return new ResponseResult<>(200, "评论成功", null);
            } catch (Exception e) { //捕获SQL执行异常
                response.setStatus(506);
                return new ResponseResult<>(506, "数据库插入失败", null);
            }
        } catch (RuntimeException e) {
            response.setStatus(403);
            return new ResponseResult<>(403, "请先登录", null);
        }
    }

    /**
     * 图书预约
     * @param libraryId
     * @param bookId
     * @return
     */
    public ResponseResult insertBookAppointment(Integer libraryId,Integer bookId){
        BookDao book = bookMapper.getLibraryBookById(libraryId, bookId);
        if(book==null){// 判断图书是否存在
            response.setStatus(505);
            return new ResponseResult<>(505, "图书不存在", null);
        }
        if(book.getNumber()<=0){ // 判断图书的库存
            response.setStatus(509);
            return new ResponseResult<>(509, "当前书籍无库存，不可预约", null);
        }
        try{
            // 获取用户
            LoginUser loginUser = getLoginUser.getLoginUser(request);
            try{
                ItemDao appointment = new ItemDao();
                appointment.setUserId(loginUser.getUser().getId());
                appointment.setTime(LocalDateTime.now());
                bookMapper.insertBookAppointment(libraryId,bookId,appointment);
                //数据
                List<ItemDao> items = bookMapper.getBookAppointment(libraryId, bookId);
                if(items==null){
                    response.setStatus(506);
                    return new ResponseResult<>(506, "查询失败", null);
                }
                // 计算预约位次
                String info = "当前您的预约在第"+items.size()+"位";
                Map<String,String> data = new HashMap<>();
                data.put("information", info);
                return new ResponseResult<Map<String, String>>(200, "预约成功", data);
            } catch (Exception e) {
                response.setStatus(507);
                return new ResponseResult<>(507, "插入失败", null);
            }
        } catch (RuntimeException e) {
            response.setStatus(403);
            return new ResponseResult<>(403, "请先登录", null);
        }
    }

    /**
     * 查询预约信息
     * @param libraryId
     * @param bookId
     * @return
     */
    public ResponseResult getBookAppointment(Integer libraryId,Integer bookId){
        List<ItemDao> appointments = bookMapper.getBookAppointment(libraryId, bookId);
        if(appointments.size()==0){ // 判断图书是否有预约记录
            response.setStatus(506);
            return new ResponseResult<>(506, "该图书没有预约信息", null);
        }
        response.setStatus(200);
        return new ResponseResult<List<ItemDao>>(200, "查询成功", appointments);
    }

    /**
     * 插入图书排队记录
     * @param libraryId
     * @param bookId
     * @return
     */
    public ResponseResult insertBookQueue(Integer libraryId,Integer bookId){
        BookDao book = bookMapper.getLibraryBookById(libraryId, bookId);
        if(book==null){
            response.setStatus(505);
            return new ResponseResult<>(505, "图书不存在", null);
        }
        if (book.getNumber() > 0) {
            response.setStatus(509);
            return new ResponseResult<>(509, "当前书籍有库存，不可排队", null);
        }
        try {
            LoginUser loginUser = getLoginUser.getLoginUser(request);
            try {
                ItemDao queue = new ItemDao();
                queue.setUserId(loginUser.getUser().getId());
                queue.setTime(LocalDateTime.now());
                bookMapper.insertBookQueue(libraryId, bookId, queue);
                // 数据
                List<ItemDao> items = bookMapper.getBookQueue(libraryId, bookId);
                if (items == null) {
                    response.setStatus(506);
                    return new ResponseResult<>(506, "查询失败", null);
                }
                String info = "当前您排在在第" + items.size() + "位";
                Map<String, String> data = new HashMap<>();
                data.put("information", info);
                return new ResponseResult<Map<String, String>>(200, "排队成功", data);
            } catch (Exception e) {
                response.setStatus(507);
                return new ResponseResult<>(507, "插入失败", null);
            }
        } catch (RuntimeException e) {
            response.setStatus(403);
            return new ResponseResult<>(403, "请先登录", null);
        }
    }

    /**
     * 查询排队信息
     * 
     * @param libraryId
     * @param bookId
     * @return
     */
    public ResponseResult getBookQueue(Integer libraryId, Integer bookId) {
        List<ItemDao> queues = bookMapper.getBookQueue(libraryId, bookId);
        if (queues.size()==0) {// 判断图书是否有排队记录
            response.setStatus(506);
            return new ResponseResult<>(506, "该图书没有排队信息", null);
        }
        response.setStatus(200);
        return new ResponseResult<List<ItemDao>>(200, "查询成功", queues);
    }
}
