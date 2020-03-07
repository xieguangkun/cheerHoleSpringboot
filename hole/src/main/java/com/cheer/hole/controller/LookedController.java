package com.cheer.hole.controller;

import com.cheer.hole.Redis.RedisLookedService;
import com.cheer.hole.enums.ResultEnum;
import com.cheer.hole.service.LikedService;
import com.cheer.hole.service.LookedService;
import com.cheer.hole.utils.ResultVoUtils;
import com.cheer.hole.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/looked")
public class LookedController {
    @Autowired
    private LookedService lookedService;

    @PostMapping("/addLooked")
    public ResultVo addLooked(@RequestParam("lookedUserId") String lookedUserId, @RequestParam("lookedPostId") String lookedPostId, @RequestParam("type") String type){
        Map<String,String> map = new HashMap<>();
        if(type.equals(LikedService.TYPE_SHARE)||type.equals(LikedService.TYPE_HIDE)){
            if(lookedService.incrementLookedCount(lookedUserId,lookedPostId,type) == 1){
                map.put("openId",lookedUserId);
                map.put("shareId",lookedPostId);
                return ResultVoUtils.success(map);
            }else{
                return ResultVoUtils.error(ResultEnum.LOOKED_BY_USER.getCode(),ResultEnum.LOOKED_BY_USER.getMessage());
            }
        }
        return ResultVoUtils.error(ResultEnum.PARAM_ARTICLE_ERROR.getCode(),ResultEnum.PARAM_ARTICLE_ERROR.getMessage());
    }
}
