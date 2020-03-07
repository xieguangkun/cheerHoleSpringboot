package com.cheer.hole.dto;

import com.cheer.hole.entity.ShareImg;
import com.cheer.hole.entity.UserHide;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class UserHideDto {
    private UserHide hide;
    private int isFav;
    private long millionSeconds;

}
