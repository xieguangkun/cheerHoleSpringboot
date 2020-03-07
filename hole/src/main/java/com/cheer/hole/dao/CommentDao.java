package com.cheer.hole.dao;

import com.cheer.hole.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentDao {

    //跟据发送文章id查评论
    List<Comment> getCommentById(@Param("shareId") String shareId);

    //获取我发送的评论
    List<Comment> getMyComment(@Param("comuserId")String comuserId);

    //删除评论
    int deleteComment(@Param("id") String id);

    //添加一条评论
    int insertComment(@Param("comment")Comment comment);
}
