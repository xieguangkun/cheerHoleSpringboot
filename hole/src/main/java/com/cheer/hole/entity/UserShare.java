package com.cheer.hole.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class UserShare{
    private String id;
    private String avatar;
    private String userName;
    private Timestamp createTime;
    private int looked;
    private String text;
    private String openId;
    private int likeNum;
    private int commNum;
    private List<ShareImg> imgList;

}
