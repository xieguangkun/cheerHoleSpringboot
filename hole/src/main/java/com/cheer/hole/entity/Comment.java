package com.cheer.hole.entity;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
@Data
public class Comment{
    private String id;
    private String shareId;
    private String comuserId;
    private String comment;
    private Timestamp createTime;
    private String commName;
    private String commAvatar;

}
