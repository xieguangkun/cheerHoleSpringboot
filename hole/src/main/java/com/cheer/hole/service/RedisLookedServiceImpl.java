package com.cheer.hole.service;

import com.cheer.hole.Redis.RedisLookedService;
import com.cheer.hole.dto.LikedCountDTO;
import com.cheer.hole.dto.LookedCountDTO;
import com.cheer.hole.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RedisLookedServiceImpl implements RedisLookedService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void saveLooked2Redis(String lookedUserId, String lookedPostId, String type) {
        String key = RedisKeyUtils.getLookedKey(lookedUserId,lookedPostId,type);
        redisTemplate.opsForHash().put(RedisKeyUtils.SET_KET_LOOKED,key,0);
    }

//    @Override
//    public void saveLookedCount2Redis(String lookedPostId, String type, int looked) {
//        String key = RedisKeyUtils.getLookedCountKey(lookedPostId,type);
//        redisTemplate.opsForHash().put(RedisKeyUtils.SET_KET_LOOKED_COUNT,key,looked);
//    }

    @Override
    public void incrementLookedCount(String lookedPostId, String type) {
        String key = RedisKeyUtils.getLookedCountKey(lookedPostId,type);
        redisTemplate.opsForHash().increment(RedisKeyUtils.SET_KET_LOOKED_COUNT,key,1);
    }

    @Override
    public Object getLookedCountValue(String lookedPostId, String type) {
        String key = RedisKeyUtils.getLookedCountKey(lookedPostId,type);
        return redisTemplate.opsForHash().get(RedisKeyUtils.SET_KET_LOOKED_COUNT,key);
    }

    @Override
    public Boolean ifExistKey(String lookedUserId, String lookedPostId, String type) {
        String key = RedisKeyUtils.getLookedKey(lookedUserId,lookedPostId,type);
        return  redisTemplate.opsForHash().hasKey(RedisKeyUtils.SET_KET_LOOKED,key);
    }

    @Override
    public List<LookedCountDTO> getLookedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.SET_KET_LOOKED_COUNT, ScanOptions.NONE);
        List<LookedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String) map.getKey();
            String[] split = key.split("::");
            String lookedPostId = split[0];
//            log.info("likedPostId这就是"+likedPostId);
            String type = split[1];
            LookedCountDTO dto = new LookedCountDTO(lookedPostId,(Integer)map.getValue(), type);
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtils.SET_KET_LOOKED_COUNT, key);
        }
        return list;
    }

    @Override
    public void clearLookedFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.SET_KET_LOOKED, ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            String key = (String) map.getKey();
            //从Redis中删除这条记录,这里保存的该用户是否浏览过的记录就不保存进database了,2天自动恢复可以增加次数
            redisTemplate.opsForHash().delete(RedisKeyUtils.SET_KET_LOOKED, key);
        }
    }
}
