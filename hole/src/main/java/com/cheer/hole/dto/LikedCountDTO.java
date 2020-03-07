package com.cheer.hole.dto;

import lombok.Data;

@Data
public class LikedCountDTO {
    private String key;
    private int count;
    private String type;

    public LikedCountDTO(){

    }

    public LikedCountDTO(String key,int count,String type){
        this.key = key;
        this.count = count;
        this.type = type;
    }
}
