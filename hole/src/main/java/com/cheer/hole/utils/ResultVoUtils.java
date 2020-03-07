package com.cheer.hole.utils;

import com.cheer.hole.vo.ResultVo;

public class ResultVoUtils {
    public static final String EMOTION_TYPE_HAPPY = "happy";
    public static final String EMOTION_TYPE_SAD = "sad";
    public static final String EMOTION_TYPE_SANG = "sang";
    public static final String EMOTION_TYPE_SURPRISE = "surprise";

    public static ResultVo success(Object object) {
        ResultVo resultVO = new ResultVo();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVo success() {
        return success(null);
    }

    public static ResultVo error(Integer code, String msg) {
        ResultVo resultVO = new ResultVo();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
