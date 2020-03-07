package com.cheer.hole.service;

import com.cheer.hole.Redis.RedisLookedService;
import com.cheer.hole.dao.UserHideDao;
import com.cheer.hole.dao.UserShareDao;
import com.cheer.hole.dto.UserHideDto;
import com.cheer.hole.dto.UserShareDto;
import com.cheer.hole.entity.ShareImg;
import com.cheer.hole.entity.UserHide;
import com.cheer.hole.entity.UserShare;
import com.cheer.hole.utils.TimeFormatUtils;
import com.cheer.hole.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserHideService {
    @Autowired
    private UserHideDao userHideDao;

    @Autowired
    private RedisLikeServiceImpl redisLikeService;

    @Autowired
    private LikedService likedService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ShareImgService imgService;

    @Autowired
    private RedisLookedServiceImpl redisLookedService;


    //新增文章
    @Transactional
    public int insertHide(String avatar,String text,String userName,String openId,String type,String[] imgList){
        //生成发送文章的随机id,图片的id自己生成，因为文章的id是要关联的
        UUIDUtils uuidUtils = new UUIDUtils();
        String shareId = uuidUtils.getUUID();
        int a = userHideDao.insertUserHide(shareId,avatar,text,userName,openId,type);
        List<ShareImg> shareImgList = new ArrayList<>();
        if(!imgList[0].equals("")) {
            for (String imgUrl : imgList) {
                ShareImg img = new ShareImg(shareId, imgUrl);
                shareImgList.add(img);
            }
            imgService.insertImg(shareImgList);
        }
        return a;
    }

    //获取所有的文章
    @Transactional
    public List<UserHideDto> getHideByEmotion(String emotion,int pageNum,String openId){
        List<UserHide> list = userHideDao.getHideByEmotion(emotion,pageNum*20);
        List<UserHideDto> dtoList = getUserHideDto(list,openId);
        return dtoList;
    }

    //跟据id查询文章
    public UserHideDto getHideById(String id,String openId){
        UserHide share = userHideDao.getHideById(id);
        List<UserHide> list = new ArrayList<>();
        list.add(share);
        List<UserHideDto> dtoList = getUserHideDto(list,openId);
        return dtoList.get(0);
    }

    //获取我发送的文章
    @Transactional
    public List<UserHideDto> getMyHide(String openid){
        List<UserHide> list = userHideDao.getMyHide(openid);
        List<UserHideDto> dtoList = getUserHideDto(list,openid);
        return dtoList;
    }

    //删除文章
    public int deleteHide(String id){
        int delete = userHideDao.deleteHide(id);
        return delete;
    }

    /**
     * 更新评论数量
     * @param commNum
     * @param commPostId
     * @return
     */
    /**
     * 更新评论数量
     * @param commPostId
     * @return
     */
    @Transactional
    public int deleteComment(String id,String commPostId){
        userHideDao.decrCommNum(commPostId);
        return commentService.deleteComment(id);
    }

    @Transactional
    public int insertComment(String id,String shareId,String commuserId,String comment,String commName,String commAvatar){
        userHideDao.incrCommNum(shareId);
        return commentService.insertComment(id,shareId,commuserId,comment,commName,commAvatar);
    }

    /**
     * 更新点赞数量
     * @param likeNum
     * @param likedPostId
     * @return
     */
    public int updateLikeNum(int likeNum,String likedPostId){
        return userHideDao.updateLikeNum(likeNum,likedPostId);
    }

    /**
     * 更新浏览次数
     * @param lookedNum
     * @param likedPostId
     * @return
     */
    public int updateLookedNum(int lookedNum,String likedPostId){
        return userHideDao.updateLookedNum(lookedNum,likedPostId);
    }


    //将数据库获取到的list转化为dto类的通用方法
    public List<UserHideDto> getUserHideDto(List<UserHide> shares,String openId){
        TimeFormatUtils timeFormatUtils = new TimeFormatUtils();
        List<UserHide> list = shares;
        UserHideDto dto;
        List<UserHideDto> dtoList = new ArrayList<>();
        for(UserHide share:list){
            dto = new UserHideDto();
            dto.setMillionSeconds(timeFormatUtils.getMillionSeconds(share.getCreateTime()));
            //从缓存like中拿值
            Object isLike = redisLikeService.getLikedValue(openId,share.getId(),LikedService.TYPE_HIDE);
            Object count = redisLikeService.getCountValue(share.getId(),LikedService.TYPE_HIDE);
            Object looked = redisLookedService.getLookedCountValue(share.getId(),LikedService.TYPE_HIDE);
            //如果缓存中有，直接设置，否则从数据库中判断，再存入缓存
            if(isLike != null){
                dto.setIsFav((int)isLike);
            }else{
                if(likedService.ifLikedByLikedUserIdAndLikedPostId(openId,share.getId())>0){
                    dto.setIsFav(1);
//                    redisLikeService.saveLiked2Redis(openId,share.getId(), LikedService.TYPE_HIDE);
                }else {
                    dto.setIsFav(0);
//                    redisLikeService.unlikeFromRedis(openId,share.getId(),LikedService.TYPE_HIDE);
                }
            }

            if(count != null){
                int likeNum = share.getLikeNum()+(int) count;
                share.setLikeNum(likeNum);
            }

            if(looked != null){
                int lookedNum = share.getLooked()+(int) looked;
                share.setLooked(lookedNum);
            }

            dto.setHide(share);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
