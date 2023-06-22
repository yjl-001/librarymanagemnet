package com.example.library.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.dao.BookDao;
import com.example.library.dao.LibraryDao;
import com.example.library.mapper.BookMapper;
import com.example.library.mapper.LibraryMapper;
import com.example.library.utils.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class LibraryService {
    @Autowired
    private LibraryMapper libraryMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private HttpServletResponse response;

    /**
     * 获取图书馆信息
     * @param libraryId
     * @return
     */
    public ResponseResult getLibraryInfo(Integer libraryId){
        LibraryDao library = libraryMapper.getLibraryInfo(libraryId);
        if(library!=null){ // 判断图书馆是否存在
            List<BookDao> collection = bookMapper.getBooksByLibraryId(libraryId);
            if(collection.size()>0){ // 判断图书馆是否有馆藏
                library.setCollection(collection);
                response.setStatus(200);
                return new ResponseResult<LibraryDao>(200, "查询成功", library);
            }else{
                response.setStatus(506);
                return new ResponseResult<LibraryDao>(506, "查询失败，图书馆无馆藏", library);
            }
        }else{
            response.setStatus(507);
            return new ResponseResult<>(507, "查询失败，图书馆不存在", null);
        }
    }

    /**
     * 获取图书馆的馆藏信息
     * @param libraryId
     * @return
     */
    public ResponseResult getLibraryCollectionInfo(Integer libraryId){
        List<BookDao> collection = bookMapper.getBooksByLibraryId(libraryId);
        if(collection.size()>0){// 判断图书馆是否有馆藏
            response.setStatus(200);
            return new ResponseResult<List<BookDao>>(200, "查询成功", collection);
        }else{
            response.setStatus(506);
            return new ResponseResult<>(506, "查询失败，该图书馆无馆藏", null);
        }
    }

    /**
     * 获取图书馆的开闭馆时间
     * @param libraryId
     * @return
     */
    public ResponseResult getLibraryOpenCloseTimeInfo(Integer libraryId){
        LibraryDao library = libraryMapper.getLibraryInfo(libraryId);
        if(library!=null){ // 判断图书馆是否存在
            Map<String,String> time = new HashMap<String,String>();
            time.put("openTime", library.getOpenTime());
            time.put("closeTime", library.getCloseTime());
            response.setStatus(200);
            return new ResponseResult<Map<String,String>>(200, "查询成功", time);
        }else{
            response.setStatus(507);
            return new ResponseResult<>(507, "查询失败，图书馆不存在", null);
        }
    }
}
