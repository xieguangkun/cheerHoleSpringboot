package com.cheer.hole.controller;

import com.cheer.hole.dto.UserShareDto;
import com.cheer.hole.entity.UserShare;
import com.cheer.hole.enums.ResultEnum;
import com.cheer.hole.service.UserShareService;
import com.cheer.hole.utils.ResultVoUtils;
import com.cheer.hole.utils.TimeFormatUtils;
import com.cheer.hole.utils.UUIDUtils;
import com.cheer.hole.vo.ResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/userShare")
public class UserShareController {
    @Autowired
    private UserShareService shareService;

    @GetMapping("/getAllShare/{openId}")
    public ResultVo getAllShare(@RequestParam("pageNum")int pageNum,@PathVariable("openId")String openId){
        List<UserShareDto> shares = shareService.getAllShare(pageNum,openId);

        if(shares == null){
            return ResultVoUtils.error(ResultEnum.SHARE_EMPTY.getCode(),ResultEnum.SHARE_EMPTY.getMessage());
        }
        return ResultVoUtils.success(shares);
    }

    @GetMapping("/getMyShare/{openId}")
    public ResultVo getMyShare(@PathVariable("openId")String openId){
        List<UserShareDto> shares = shareService.getMyShare(openId);
        if(shares == null){
            return ResultVoUtils.error(ResultEnum.SHARE_EMPTY.getCode(),ResultEnum.SHARE_EMPTY.getMessage());
        }
        return ResultVoUtils.success(shares);
    }

    @GetMapping("/getShareById/{openId}/{id}")
    public ResultVo getShareById(@PathVariable("id")String id,@PathVariable("openId")String openId){
        UserShareDto share = shareService.getShareById(id,openId);

        if(share == null){
            return ResultVoUtils.error(ResultEnum.SHARE_EMPTY.getCode(),ResultEnum.SHARE_EMPTY.getMessage());
        }
        return ResultVoUtils.success(share);
    }

    @PostMapping("/deleteShare/{id}")
    public ResultVo deleteShare(@PathVariable("id")String id){
        Map<String,String> map = new HashMap<>();
        int delete = shareService.deleteShare(id);
        if(delete == 1){
            map.put("id",id);
            return ResultVoUtils.success(map);
        }else{
            return ResultVoUtils.error(ResultEnum.SHARE_NOT_EXIST.getCode(),ResultEnum.SHARE_NOT_EXIST.getMessage());
        }
    }

    @PostMapping("/insertShare")
    public ResultVo insertShare(@RequestParam("avatar") String avatar, @RequestParam("text") String text, @RequestParam("userName") String userName, @RequestParam("openId") String openId,@RequestParam("imgList")String imgList){
//        JSONObject jsonObject = new JSONObject();
//        List<String> list  = new ArrayList<>();
//
        Map<String,String> map = new HashMap<>();
        String []list = imgList.split(",");
        if(shareService.insertShare(avatar,text,userName,openId,list) == 1){
            map.put("avatar",avatar);
            map.put("text",text);
            map.put("userName",userName);
            map.put("openId",openId);
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

    @PostMapping("/deleteCommentByShare/{id}/{commPostId}")
    public ResultVo deleteComment(@PathVariable("id")String id, @PathVariable("commPostId")String shareId){
        Map<String,String> map = new HashMap<>();
//        ResultVo vo = new ResultVo();
        if(shareService.deleteComment(id,shareId)==1){
            map.put("id",id);
            map.put("userShareId",shareId);
            map.put("删除评论成功","OK");
            return ResultVoUtils.success(map);
        }else{
            return ResultVoUtils.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
    }

    @PostMapping("/insertCommentByShare")
    public ResultVo insertComment(@RequestParam("shareId") String shareId, @RequestParam("commuserId")String commuserId, @RequestParam("comment")String comment, @RequestParam("commName")String commName, @RequestParam("commAvatar")String commAvatar){
        UUIDUtils uuidUtils = new UUIDUtils();
        Map<String,String> map = new HashMap<>();
        String id = uuidUtils.getUUID();
        if(shareService.insertComment(id,shareId,commuserId,comment,commName,commAvatar) == 1){
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
