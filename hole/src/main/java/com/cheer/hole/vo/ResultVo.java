package com.cheer.hole.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVo<T> implements Serializable {
    private static final long serialVersionUID = 2302843591802914221L;
    /**数据 */
    private T data;
    /**错误信息 */
    private Integer code;
    /**提示信息*/
    private String msg;

}
