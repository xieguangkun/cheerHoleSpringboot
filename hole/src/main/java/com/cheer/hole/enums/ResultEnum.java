package com.cheer.hole.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
        SUCCESS(0, "成功"),

        PARAM_TYPE_ERROR(1, "类型参数不正确"),

        PARAM_ARTICLE_ERROR(2,"type应传递share或hide"),

        PARAM_ERROR(3,"参数传递错误"),

        SHARE_NOT_EXIST(10, "操作的消息不存在"),

        LIKE_REPEAT_ERROR(11,"已点过赞"),

        UNLIKE_REPEAT_ERROR(12,"已取消过点赞"),

        LOOKED_BY_USER(13,"已被用户浏览过"),

        DELETED_ERROR(14,"删除失败"),

        EMOTION_TYPE_ERROR(15,"心情应传递happy或者say或者sang或surprise"),

        INSERT_FAIL_ERROR(16,"新增文章失败"),

        SHARE_EMPTY(18, "获取的文章为空"),

        ;

        private Integer code;

        private String message;

        ResultEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
}
