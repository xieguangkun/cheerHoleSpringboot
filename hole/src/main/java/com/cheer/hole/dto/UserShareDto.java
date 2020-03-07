package com.cheer.hole.dto;

import com.cheer.hole.entity.ShareImg;
import com.cheer.hole.entity.UserShare;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class UserShareDto {
    private UserShare share;
    private int isFav;
    private long millionSeconds;

}
