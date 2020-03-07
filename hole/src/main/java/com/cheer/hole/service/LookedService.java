package com.cheer.hole.service;

import com.cheer.hole.Redis.RedisLookedService;
import com.cheer.hole.dto.LikedCountDTO;
import com.cheer.hole.dto.LookedCountDTO;
import com.cheer.hole.entity.Like;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LookedService {

    @Autowired
    private RedisLookedService redisLookedService;

    @Autowired
    private UserShareService shareService;

    @Autowired
    private UserHideService hideService;

    @Transactional
    public int incrementLookedCount(String lookedUserId,String lookedPostId,String type){
        if(!redisLookedService.ifExistKey(lookedUserId,lookedPostId,type)){
            redisLookedService.incrementLookedCount(lookedPostId,type);
            redisLookedService.saveLooked2Redis(lookedUserId,lookedPostId,type);
            return 1;
        }
        return 0;
    }

    public void transLookedCountFromRedis2DB(){
        List<LookedCountDTO> list = redisLookedService.getLookedCountFromRedis();
        if(list.size() == 0){
            return;
        }
        for (LookedCountDTO dto : list) {
            //如果是非匿名文章就更新非匿名
            if(dto.getType().equals(LikedService.TYPE_SHARE)) {
                shareService.updateLookedNum(dto.getCount(),dto.getKey());
            }
            //如果是匿名文章就更新匿名
            if(dto.getType().equals(LikedService.TYPE_HIDE)){
                hideService.updateLookedNum(dto.getCount(),dto.getKey());
            }
        }
    }
}

