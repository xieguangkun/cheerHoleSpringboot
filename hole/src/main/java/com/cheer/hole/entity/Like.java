package com.cheer.hole.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Like{
    /**
     * 点赞数据默认为0(即为未点赞）,(如果点了赞就是1)
     */
    private String id;
    private String likedUserId;
    private String likedPostId;
    private String type;
    private Integer status = 0;

    public Like() {

    }

    public Like(String likedUserId, String likedPostId,String type,Integer status) {
        this.likedUserId = likedUserId;
        this.likedPostId = likedPostId;
        this.type = type;
        this.status = status;
    }

}
