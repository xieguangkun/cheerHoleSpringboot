package com.cheer.hole.controller;

import com.cheer.hole.dto.UserHideDto;
import com.cheer.hole.entity.UserHide;
import com.cheer.hole.entity.UserShare;
import com.cheer.hole.enums.ResultEnum;
import com.cheer.hole.service.UserHideService;
import com.cheer.hole.utils.ResultVoUtils;
import com.cheer.hole.utils.UUIDUtils;
import com.cheer.hole.vo.ResultVo;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userHide")
public class UserHideController {
    @Autowired
    private UserHideService hideService;

    @PostMapping(value = "/insertHide")
    public ResultVo insertHide(@RequestParam("avatar") String avatar, @RequestParam("text") String text, @RequestParam("userName") String userName, @RequestParam("openId") String openId, @RequestParam("type")String type, @RequestParam("imgList") String imgList){
        if(type.equals(ResultVoUtils.EMOTION_TYPE_HAPPY) || type.equals(ResultVoUtils.EMOTION_TYPE_SAD)||type.equals(ResultVoUtils.EMOTION_TYPE_SANG)||type.equals(ResultVoUtils.EMOTION_TYPE_SURPRISE)){
            Map<String,String> map = new HashMap<>();
            String []list = imgList.split(",");
            if(hideService.insertHide(avatar,text,userName,openId,type,list) == 1){
                map.put("avatar",avatar);
                map.put("text",text);
                map.put("userName",userName);
                map.put("openId",openId);
                map.put("type",type);
                StringBuilder builder = new StringBuilder();
                for(String img:list){
                    builder.append(img+" ");
                }
                map.put("imgList",builder.toString());
                return ResultVoUtils.success(map);
            }else{
                return ResultVoUtils.error(ResultEnum.INSERT_FAIL_ERROR.getCode(),ResultEnum.INSERT_FAIL_ERROR.getMessage());
            }
        }
        return ResultVoUtils.error(ResultEnum.EMOTION_TYPE_ERROR.getCode(),ResultEnum.EMOTION_TYPE_ERROR.getMessage());
    }

    @GetMapping("/getHideByEmotion/{openId}/{emotion}")
    public ResultVo getHideByEmotion(@PathVariable("emotion")String emotion,@RequestParam("pageNum")int pageNum,@PathVariable("openId")String openId){
        if(!emotion.equals(ResultVoUtils.EMOTION_TYPE_HAPPY)&&!emotion.equals(ResultVoUtils.EMOTION_TYPE_SAD)&&!emotion.equals(ResultVoUtils.EMOTION_TYPE_SANG)&&!emotion.equals(ResultVoUtils.EMOTION_TYPE_SURPRISE)){
            return ResultVoUtils.error(ResultEnum.EMOTION_TYPE_ERROR.getCode(),ResultEnum.EMOTION_TYPE_ERROR.getMessage());
        }
        List<UserHideDto> shares = hideService.getHideByEmotion(emotion,pageNum,openId);
        if(shares == null){
            return ResultVoUtils.error(ResultEnum.SHARE_EMPTY.getCode(),ResultEnum.SHARE_EMPTY.getMessage());
        }

        return ResultVoUtils.success(shares);
    }

    @GetMapping("/getMyHide/{openId}")
    public ResultVo getMyShare(@PathVariable("openId")String openId){
        List<UserHideDto> shares = hideService.getMyHide(openId);
        if(shares == null){
            return ResultVoUtils.error(ResultEnum.SHARE_EMPTY.getCode(),ResultEnum.SHARE_EMPTY.getMessage());
        }
        return ResultVoUtils.success(shares);
    }

    @GetMapping("/getHideById/{openId}/{id}")
    public ResultVo getHideById(@PathVariable("id")String id,@PathVariable("openId")String openId){
        UserHideDto share = hideService.getHideById(id,openId);
        if(share == null){
            return ResultVoUtils.error(ResultEnum.SHARE_EMPTY.getCode(),ResultEnum.SHARE_EMPTY.getMessage());
        }
        return ResultVoUtils.success(share);
    }

    @PostMapping("/deleteHide/{id}")
    public ResultVo deleteHide(@PathVariable("id")String id){
        Map<String,String> map = new HashMap<>();
        int delete = hideService.deleteHide(id);
        if(delete == 1){
            map.put("id",id);
            return ResultVoUtils.success(map);
        }else{
            return ResultVoUtils.error(ResultEnum.SHARE_NOT_EXIST.getCode(),ResultEnum.SHARE_NOT_EXIST.getMessage());
        }
    }

    @PostMapping("/deleteCommentByHide/{id}/{commPostId}")
    public ResultVo deleteComment(@PathVariable("id")String id,@PathVariable("commPostId")String shareId){
        Map<String,String> map = new HashMap<>();
        if(hideService.deleteComment(id,shareId)==1){
            map.put("id",id);
            map.put("userShareId",shareId);
            map.put("删除评论成功","OK");
            return ResultVoUtils.success(map);
        }else{
            return ResultVoUtils.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
    }

    @PostMapping("/insertCommentByHide")
    public ResultVo insertComment(@RequestParam("shareId") String shareId, @RequestParam("commuserId")String commuserId, @RequestParam("comment")String comment, @RequestParam("commName")String commName, @RequestParam("commAvatar")String commAvatar){
        UUIDUtils uuidUtils = new UUIDUtils();
        Map<String,String> map = new HashMap<>();
        String id = uuidUtils.getUUID();
        if(hideService.insertComment(id,shareId,commuserId,comment,commName,commAvatar) == 1){
            map.put("id",id);
            map.put("shareId",shareId);
            map.put("comuserId",commuserId);
            map.put("comment",comment);
            map.put("commName",commName);
            map.put("commAvatar",commAvatar);
            return ResultVoUtils.success(map);
        }else{
            return ResultVoUtils.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
    }
}
