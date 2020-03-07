package com.cheer.hole.service;

import com.cheer.hole.dao.ShareImgDao;
import com.cheer.hole.entity.ShareImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareImgService {
    @Autowired
    private ShareImgDao imgDao;

    //添加照片
    public int insertImg(List<ShareImg> list){
        return imgDao.insertImg(list);
    }
}
