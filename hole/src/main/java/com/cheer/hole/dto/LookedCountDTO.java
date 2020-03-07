package com.cheer.hole.dto;

import lombok.Data;

@Data
public class LookedCountDTO {
    private String key;
    private int count;
    private String type;

    public LookedCountDTO(){

    }

    public LookedCountDTO(String key,int count,String type){
        this.key = key;
        this.count = count;
        this.type = type;
    }
}
