package com.cheer.hole.exception;

import com.cheer.hole.enums.ResultEnum;

public class LikeException extends RuntimeException{
    private Integer code;

    public LikeException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public LikeException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
