package com.cheer.hole.service;

import com.cheer.hole.Redis.RedisLikeService;
import com.cheer.hole.dto.LikedCountDTO;
import com.cheer.hole.entity.Like;
import com.cheer.hole.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RedisLikeServiceImpl implements RedisLikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void saveLiked2Redis(String likedUserId, String likedPostId, String type) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId, type);
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, 1);
    }

    @Override
    public void unlikeFromRedis(String likedUserId, String likedPostId, String type) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId, type);
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, 0);
    }

    @Override
    public void deleteLikedFromRedis(String likedUserId, String likedPostId, String type) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId, type);
        redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
    }

//    @Override
//    public void putLikedCount2Redis(String likedPostId, String type, int num) {
//        String key = RedisKeyUtils.getCountKey(likedPostId, type);
//        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT,key, num);
//    }

    @Override
    public void deleteLikedCountFromRedis(String likedPostId, String type) {
        String key = RedisKeyUtils.getCountKey(likedPostId, type);
        redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key);
    }

//    @Override
//    public void setCountToRedis(String likedPostId, String type,int count) {
//        String key = RedisKeyUtils.getCountKey(likedPostId, type);
//        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key,count);
//    }

    @Override
    public void incrementLikedCount(String likedPostId, String type) {
        String key = RedisKeyUtils.getCountKey(likedPostId, type);
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key, 1);
    }

    @Override
    public void decrementLikedCount(String likedPostId, String type) {
        String key = RedisKeyUtils.getCountKey(likedPostId, type);
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key, -1);
    }

    @Override
    public Object getLikedValue(String likedUserId, String likedPostId, String type) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId, type);
        return redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED,key);
    }

    @Override
    public Object getCountValue(String likedPostId, String type) {
        String key = RedisKeyUtils.getCountKey(likedPostId, type);
        return redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT,key);
    }


    @Override
    public List<Like> getLikedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_USER_LIKED, ScanOptions.NONE);
        List<Like> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 likedUserId，likedPostId,type
            String[] split = key.split("::");
            String likedUserId = split[0];
            String likedPostId = split[1];
            String type = split[2];
            Integer value = (Integer) entry.getValue();

            //组装成 UserLike 对象
            Like userLike = new Like(likedUserId, likedPostId, type, value);
            list.add(userLike);

            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
        }

        return list;
    }

    @Override
    public List<LikedCountDTO> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<LikedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String) map.getKey();
            String[] split = key.split("::");
            String likedPostId = split[0];
//            log.info("likedPostId这就是"+likedPostId);
            String type = split[1];
            LikedCountDTO dto = new LikedCountDTO(likedPostId,(Integer)map.getValue(), type);
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }

    @Override
    public List<Like> getMyLikedFromRedis(String myLikedUserId) {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<Like> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 likedUserId，likedPostId,type
            String[] split = key.split("::");
            String likedUserId = split[0];
            String likedPostId = split[1];
            String type = split[2];
            Integer value = (Integer) entry.getValue();
            if (likedUserId == myLikedUserId) {
                //组装成 UserLike 对象
                Like userLike = new Like(likedUserId, likedPostId, type, value);
                list.add(userLike);
            }
        }
        return list;
    }
}
