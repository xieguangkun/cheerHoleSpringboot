package com.cheer.hole.service;

import com.cheer.hole.Redis.RedisLookedService;
import com.cheer.hole.dao.UserShareDao;
import com.cheer.hole.dto.UserShareDto;
import com.cheer.hole.entity.ShareImg;
import com.cheer.hole.entity.UserShare;
import com.cheer.hole.utils.TimeFormatUtils;
import com.cheer.hole.utils.UUIDUtils;
import com.mchange.v2.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserShareService {
    @Autowired
    private UserShareDao userShareDao;

    @Autowired
    private LikedService likedService;

    @Autowired
    private RedisLikeServiceImpl redisLikeService;

    @Autowired
    private ShareImgService imgService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisLookedService redisLookedService;


    //新增文章
    @Transactional
    public int insertShare(String avatar,String text,String userName,String openId,String[] imgList){
        //生成发送文章的随机id,图片的id自己生成，因为文章的id是要关联的
        UUIDUtils uuidUtils = new UUIDUtils();
        String shareId = uuidUtils.getUUID();
        int a = userShareDao.insertUserShare(shareId,avatar,text,userName,openId);
        if(!imgList[0].equals("")) {
            List<ShareImg> shareImgList = new ArrayList<>();
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
    public List<UserShareDto> getAllShare(int pageNum,String openId){
        List<UserShare> list = userShareDao.getAllShare(pageNum*20);
        List<UserShareDto> dtoList = getUserShareDto(list,openId);
        return dtoList;
    }

    //跟据id查询文章
    public UserShareDto getShareById(String id,String openId){
        UserShare share = userShareDao.getShareById(id);
        List<UserShare> list = new ArrayList<>();
        list.add(share);
        List<UserShareDto> dtoList = getUserShareDto(list,openId);
        return dtoList.get(0);
    }

    /**
     * 获取我发送的文章
     * @param openId
     * @return
     */
    @Transactional
    public List<UserShareDto> getMyShare(String openId){
        List<UserShare> list = userShareDao.getMyShare(openId);
        List<UserShareDto> dtoList = getUserShareDto(list,openId);
        return dtoList;
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    public int deleteShare(String id){
        int delete = userShareDao.deleteShare(id);
        return delete;
    }

    /**
     * 更新评论数量
     * @param commPostId
     * @return
     */
    @Transactional
    public int deleteComment(String id,String commPostId){
       userShareDao.decrCommNum(commPostId);
       return commentService.deleteComment(id);
    }

    /**
     * 新增评论
     * @param id
     * @param shareId
     * @param commuserId
     * @param comment
     * @param commName
     * @param commAvatar
     * @return
     */
    @Transactional
    public int insertComment(String id,String shareId,String commuserId,String comment,String commName,String commAvatar){
        userShareDao.incrCommNum(shareId);
        return commentService.insertComment(id,shareId,commuserId,comment,commName,commAvatar);
    }

    /**
     * 更新点赞数量
     * @param likeNum
     * @param likedPostId
     * @return
     */
    public int updateLikeNum(int likeNum,String likedPostId){
         return userShareDao.updateLikeNum(likeNum,likedPostId);
    }

    /**
     * 更新浏览次数
     * @param lookedNum
     * @param lookedPostId
     */
    public int updateLookedNum(int lookedNum,String lookedPostId){
        return userShareDao.updateLookedNum(lookedNum,lookedPostId);
    }


    //将数据库获取到的list转化为dto类的通用方法
    public List<UserShareDto> getUserShareDto(List<UserShare> shares,String openId){
        TimeFormatUtils timeFormatUtils = new TimeFormatUtils();
        List<UserShare> list = shares;
        UserShareDto dto;
        List<UserShareDto> dtoList = new ArrayList<>();
        for(UserShare share:list){
            dto = new UserShareDto();
            dto.setMillionSeconds(timeFormatUtils.getMillionSeconds(share.getCreateTime()));
            //从缓存like中拿值
            Object isLike = redisLikeService.getLikedValue(openId,share.getId(), LikedService.TYPE_SHARE);
            Object count = redisLikeService.getCountValue(share.getId(),LikedService.TYPE_SHARE);
            Object looked = redisLookedService.getLookedCountValue(share.getId(),LikedService.TYPE_SHARE);
            //如果缓存中有，直接设置，否则从数据库中判断，再存入缓存
            if(isLike != null){
                dto.setIsFav((int)isLike);
            }else{
                if(likedService.ifLikedByLikedUserIdAndLikedPostId(openId,share.getId())>0){
                    dto.setIsFav(1);
//                    redisLikeService.saveLiked2Redis(openId,share.getId(), LikedService.TYPE_SHARE);
                }else {
                    dto.setIsFav(0);
//                    redisLikeService.unlikeFromRedis(openId,share.getId(), LikedService.TYPE_SHARE);
                }
            }

            if(count != null){
                int likedNum = share.getLikeNum()+(int) count;
                share.setLikeNum(likedNum);
            }

            if(looked != null){
                int lookedNum = share.getLooked()+ (int) looked;
                share.setLooked(lookedNum);
            }

            dto.setShare(share);
            dtoList.add(dto);
        }
        return dtoList;
    }

}
