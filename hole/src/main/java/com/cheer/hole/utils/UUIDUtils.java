package com.cheer.hole.utils;

import java.util.UUID;

//uuid即为随机id生成器
public class UUIDUtils {
        public String getUUID(){
            String uuid = UUID.randomUUID().toString().replace("-", "");
            return uuid;
        }

    }
