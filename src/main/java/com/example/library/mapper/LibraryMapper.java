package com.example.library.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.library.dao.LibraryDao;

@Mapper
public interface LibraryMapper {
    /**
     * 查询图书馆信息
     * @param libraryId
     * @return
     */
    @Select("SELECT * FROM library WHERE id = #{libraryId}")
    LibraryDao getLibraryInfo(@Param(value = "libraryId") Integer libraryId);
}
