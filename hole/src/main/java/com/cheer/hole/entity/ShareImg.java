package com.cheer.hole.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class ShareImg{
    private String id;
    private String shareId;
    private String imgUrl;

    public ShareImg(){

    }

    public ShareImg(String shareId,String imgUrl){
        this.shareId = shareId;
        this.imgUrl = imgUrl;
    }
}
