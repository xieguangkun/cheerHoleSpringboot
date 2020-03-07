package com.cheer.hole.utils;

public class RedisKeyUtils {
    //保存用户点赞数据的key
    public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";
    //保存用户被点赞数量的key
    public static final String MAP_KEY_USER_LIKED_COUNT = "MAP_USER_LIKED_COUNT";
    //保存浏览键值
    public static final String SET_KET_LOOKED = "SET_LOOKED";
    //保存浏览次数
    public static final String SET_KET_LOOKED_COUNT = "SET_LOOKED_COUNT";

    public static final long LOOKED_EXPIRE_TIME = 604800;

    /**
     * 拼接被点赞的用户id和点赞的人的id作为key。格式 222222::333333
     *
     * @param likedUserId 被点赞的人id
     * @param likedPostId 点赞的人的id
     * @param  type 判断类型为匿名还是普通文章
     * @return
     */
    public static String getLikedKey(String likedUserId, String likedPostId,String type) {
        StringBuilder builder = new StringBuilder();
        builder.append(likedUserId);
        builder.append("::");
        builder.append(likedPostId);
        builder.append("::");
        builder.append(type);
        return builder.toString();
    }

    /**
     * 插入文章的被浏览次数key的生成
     * @param likedPostId
     * @param type
     * @return
     */
    public static String getCountKey(String likedPostId,String type) {
        StringBuilder builder = new StringBuilder();
        builder.append(likedPostId);
        builder.append("::");
        builder.append(type);
        return builder.toString();
    }

    /**
     * 转化为浏览记录的key
     * @param lookedUserId
     * @param lookedPostId
     * @param type
     * @return
     */
    public static String getLookedKey(String lookedUserId,String lookedPostId,String type){
        StringBuilder builder = new StringBuilder();
        builder.append(lookedUserId);
        builder.append("::");
        builder.append(lookedPostId);
        builder.append("::");
        builder.append(type);
        builder.append("::");
        builder.append("looked");
        return builder.toString();
    }

    /**
     * 转化为浏览次数的key
     * @param lookedPostId
     * @param type
     * @return
     */
    public static String getLookedCountKey(String lookedPostId,String type){
        StringBuilder builder = new StringBuilder();
        builder.append(lookedPostId);
        builder.append("::");
        builder.append(type);
        builder.append("::");
        builder.append("looked");
        return builder.toString();
    }

}
