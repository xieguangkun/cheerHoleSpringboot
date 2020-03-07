package com.cheer.hole.controller;

import com.cheer.hole.entity.Comment;
import com.cheer.hole.service.CommentService;
import com.cheer.hole.service.UserShareService;
import com.cheer.hole.utils.ResultVoUtils;
import com.cheer.hole.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/getCommentById/{shareId}")
    public ResultVo getCommentById(@PathVariable("shareId") String shareId){
        List<Comment> comments = commentService.getCommentById(shareId);
        return ResultVoUtils.success(comments);
    }

    @GetMapping("/getMyComment/{comuserId}")
    public ResultVo getMyComment(@PathVariable("comuserId") String comuserId){
        List<Comment> comments = commentService.getCommentById(comuserId);
        return ResultVoUtils.success(comments);
    }

}
