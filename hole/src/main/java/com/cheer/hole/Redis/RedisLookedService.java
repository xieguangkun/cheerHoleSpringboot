package com.cheer.hole.Redis;

import com.cheer.hole.dto.LookedCountDTO;

import java.util.List;

public interface RedisLookedService {
    /**
     * 将浏览记录设置到redis中
     * @param lookedUserId
     * @param lookedPostId
     * @param type
     */
    void saveLooked2Redis(String lookedUserId, String lookedPostId,String type);

//    /**
//     * 将浏览次数设置到redis中
//     * @param lookedPostId
//     * @param type
//     * @param looked
//     */
//    void saveLookedCount2Redis(String lookedPostId,String type,int looked);

    /**
     * 该增加一条浏览次数
     * @param lookedPostId
     * @param type
     */
    void incrementLookedCount(String lookedPostId,String type);

    /**
     * 获取浏览次数的value
     * @param lookedPostId
     * @param type
     * @return
     */
    Object getLookedCountValue(String lookedPostId,String type);

    /**
     * 检查是否含有key
     * @param lookedUserId
     * @param lookedPostId
     * @param type
     * @return
     */
    Boolean ifExistKey(String lookedUserId,String lookedPostId,String type);

    /**
     * 获取redis中的浏览次数
     * @return
     */
    List<LookedCountDTO> getLookedCountFromRedis();

    /**
     * 清除2天的用户浏览次数增加限制
     */
    void clearLookedFromRedis();
}
