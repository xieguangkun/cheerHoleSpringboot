package com.cheer.hole.controller;

import com.cheer.hole.entity.Like;
import com.cheer.hole.enums.ResultEnum;
import com.cheer.hole.exception.LikeException;
import com.cheer.hole.service.LikedService;
import com.cheer.hole.service.RedisLikeServiceImpl;
import com.cheer.hole.utils.ResultVoUtils;
import com.cheer.hole.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/liked")
public class LikedController {
    @Autowired
    private LikedService likedService;
    @Autowired
    private RedisLikeServiceImpl redisLikeService;

//    public int save(Like like) {
//        return likedService.save(like);
//    }

    @GetMapping("/getShareLikedListByLikedUserId/{likedUserId}")
    public ResultVo getShareLikedListByLikedUserId(@PathVariable("likedUserId") String likedUserId) throws Exception {
        List<Like> likes = likedService.getShareLikedListByLikedUserId(likedUserId);
        return ResultVoUtils.success(likes);
    }

    @GetMapping("/getHideLikedListByLikedPostId/{likedUserId}")
    public ResultVo getHideLikedListByLikedPostId(@PathVariable("likedUserId") String likedUserId) throws Exception {
        List<Like> likes = likedService.getHideLikedListByLikedUserId(likedUserId);
        return ResultVoUtils.success(likes);
    }

//    @RequestMapping("/updateLikedStatus")
//    public int updateLikedStatus(@RequestParam("status") int status,@RequestParam("likedPostId")String likedPostId){
//        return likedService.updateLikedStatus(status,likedPostId);
//    }
//
//    @RequestMapping("/getByLikedUserIdAndLikedPostId/{likedUserId}/{likedPostId}")
//    public int getByLikedUserIdAndLikedPostId(@PathVariable("likedUserId") String likedUserId, @PathVariable("likedPostId") String likedPostId) {
//        return likedService.getByLikedUserIdAndLikedPostId(likedUserId, likedPostId);
//    }

    @PostMapping("/like/{type}/{likedUserId}/{likedPostId}")
    public ResultVo like(@PathVariable("likedUserId") String likedUserId, @PathVariable("likedPostId") String likedPostId,@PathVariable("type")String type){
        if(type.equals(LikedService.TYPE_SHARE)||type.equals(LikedService.TYPE_HIDE)){
            try {
                likedService.like(likedUserId,likedPostId,type);
                return ResultVoUtils.success(1);
            }catch (LikeException e){
                return ResultVoUtils.error(e.hashCode(),e.getMessage());
            }
        }
        return ResultVoUtils.error(ResultEnum.PARAM_ARTICLE_ERROR.getCode(),ResultEnum.PARAM_ARTICLE_ERROR.getMessage());
    }

    @PostMapping("/unlike/{type}/{likedUserId}/{likedPostId}")
    public ResultVo unlike(@PathVariable("likedUserId") String likedUserId, @PathVariable("likedPostId") String likedPostId,@PathVariable("type")String type){
//        Map<String,String> map = new HashMap<>();
        if(type.equals(LikedService.TYPE_SHARE)||type.equals(LikedService.TYPE_HIDE)){
            try {
                likedService.unlike(likedUserId,likedPostId,type);
                return ResultVoUtils.success(1);
            }catch (LikeException e){
                return ResultVoUtils.error(e.hashCode(),e.getMessage());
            }
        }
        return ResultVoUtils.error(ResultEnum.PARAM_ARTICLE_ERROR.getCode(),ResultEnum.PARAM_ARTICLE_ERROR.getMessage());
    }



}
