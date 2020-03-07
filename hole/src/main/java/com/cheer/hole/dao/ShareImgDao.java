package com.cheer.hole.dao;

import com.cheer.hole.entity.ShareImg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareImgDao{
    int insertImg(@Param("list")List<ShareImg> list);
}
