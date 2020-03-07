package com.cheer.hole.Redis;

import com.cheer.hole.dto.LikedCountDTO;
import com.cheer.hole.entity.Like;

import java.util.List;

public interface RedisLikeService {
    /**
     * 点赞。状态为1
     * @param likedUserId
     * @param likedPostId
     * @param type
     */
    void saveLiked2Redis(String likedUserId, String likedPostId,String type);

    /**
     * 取消点赞。将状态改变为0
     * @param likedUserId
     * @param likedPostId
     * @param type
     */
    void unlikeFromRedis(String likedUserId, String likedPostId,String type);

    /**
     * 从Redis中删除一条点赞数据
     * @param likedUserId
     * @param likedPostId
     * @param type
     */
    void deleteLikedFromRedis(String likedUserId, String likedPostId,String type);

//    /**
//     * 增加数据的点赞数量到redis
//     * @param likedPostId
//     * @param type
//     */
//    void putLikedCount2Redis(String likedPostId,String type,int num);

    /**
     * 将点赞数量从redis中删除
     * @param likedPostId
     * @param type
     */
    void deleteLikedCountFromRedis(String likedPostId,String type);

//    /**
//     * 将数据库中count的值设到redis中
//     * @param likedPostId
//     * @param type
//     */
//    void setCountToRedis(String likedPostId,String type,int count);

    /**
     * 该文章的点赞数加1
     * @param likedPostId
     * @param type
     */
    void incrementLikedCount(String likedPostId,String type);

    /**
     * 该文章的点赞数减1
     * @param likedPostId
     * @param type
     */
    void decrementLikedCount(String likedPostId,String type);

    /**
     * 获取点赞的value
     * @param likedUserId
     * @param likedPostId
     * @param type
     * @return
     */
    Object getLikedValue(String likedUserId,String likedPostId,String type);

    /**
     * 获取点赞的数量
     * @param likedPostId
     * @param type
     * @return
     */
    Object getCountValue(String likedPostId,String type);
    /**
     * 获取Redis中存储的所有点赞数据
     * @return
     */
    List<Like> getLikedDataFromRedis();

    /**
     * 获取Redis中存储的所有点赞数量
     * @return
     */
    List<LikedCountDTO> getLikedCountFromRedis();

    /**
     * 获取当前的点赞数据
     * @param likedUserId
     * @return
     */
    List<Like> getMyLikedFromRedis(String likedUserId);

}
