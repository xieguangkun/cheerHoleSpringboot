package com.cheer.hole.service;

import com.cheer.hole.dao.CommentDao;
import com.cheer.hole.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserShareService shareService;

    public List<Comment> getCommentById(String shareId){
        return commentDao.getCommentById(shareId);
    }

    public List<Comment> getMyComment(String comuserId){
        return commentDao.getMyComment(comuserId);
    }

    @Transactional
    public int deleteComment(String id){
        return commentDao.deleteComment(id);
    }

    @Transactional
    public int insertComment(String id,String shareId,String commuserId,String comment,String commName,String commAvatar){
        Comment oneComment = new Comment();
        oneComment.setId(id);
        oneComment.setShareId(shareId);
        oneComment.setComuserId(commuserId);
        oneComment.setComment(comment);
        oneComment.setCommName(commName);
        oneComment.setCommAvatar(commAvatar);
        return commentDao.insertComment(oneComment);
    }

}
