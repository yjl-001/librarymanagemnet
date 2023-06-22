package com.example.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.service.LibraryService;
import com.example.library.utils.ResponseResult;


/**
 * 图书馆管理模块的接口
 * 接口访问需要有admin权限
 */
@RestController
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    /**
     * 查询图书馆的信息
     * @param libraryId
     * @return
     */
    @RequestMapping(value = "/library/information",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getLibraryInfo(@RequestParam("libraryId") Integer libraryId){
        return libraryService.getLibraryInfo(libraryId);
    }

    /**
     * 查询某图书馆的馆藏信息
     * @param libraryId
     * @return
     */
    @RequestMapping(value = "/library/collection",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getLibraryCollectionInfo(@RequestParam("libraryId") Integer libraryId){
        return libraryService.getLibraryCollectionInfo(libraryId);
    }

    /**
     * 查询某图书馆的开闭馆时间
     * @param libraryId
     * @return
     */
    @RequestMapping(value = "/library/open-close-time",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getLibraryOpenCloseTimeInfo(@RequestParam("libraryId") Integer libraryId){
        return libraryService.getLibraryOpenCloseTimeInfo(libraryId);
    }
}
