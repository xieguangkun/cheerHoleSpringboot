package com.cheer.hole.service;

import com.cheer.hole.dao.LikedDao;
import com.cheer.hole.dto.LikedCountDTO;
import com.cheer.hole.entity.Like;
import com.cheer.hole.entity.UserHide;
import com.cheer.hole.entity.UserShare;
import com.cheer.hole.enums.ResultEnum;
import com.cheer.hole.exception.LikeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Service
public class LikedService {

    public static final String TYPE_SHARE = "share";

    public static final String TYPE_HIDE = "hide";

    @Autowired
    private LikedDao likedDao;
    @Autowired
    private RedisLikeServiceImpl redisLikeService;
    @Autowired
    private UserShareService shareService;
    @Autowired
    private UserHideService hideService;

    @Transactional
    public void save(Like like) {
        likedDao.insertLike(like);
    }


    public List<Like> getShareLikedListByLikedUserId(String likedUserId) {

        return likedDao.getMyLikeFromShare(likedUserId);
    }

    public List<Like> getHideLikedListByLikedUserId(String likedUserId) {
        return likedDao.getMyLikeFromHide(likedUserId);
    }

    public int updateLikedStatus(int status,String likedPostId,String likedUserId){
        return likedDao.updateLikeStatus(status,likedPostId,likedUserId);
    }


    public int getByLikedUserIdAndLikedPostId(String likedUserId, String likedPostId) {
        return likedDao.getByLikedUserIdAndLikedPostId(likedUserId, likedPostId);
    }

    public int ifLikedByLikedUserIdAndLikedPostId(String likedUserId, String likedPostId){
        return likedDao.ifLikedByLikedUserIdAndLikedPostId(likedUserId, likedPostId);
    }

    @Transactional
    public void like(String likedUserId,String likedPostId,String type) throws LikeException{
        if(!type.equals(LikedService.TYPE_SHARE)&&!type.equals(LikedService.TYPE_HIDE)){
            throw new LikeException(ResultEnum.PARAM_ARTICLE_ERROR);
        }
        if(redisLikeService.getLikedValue(likedUserId,likedPostId,type) == null){
            if (ifLikedByLikedUserIdAndLikedPostId(likedUserId, likedPostId) == 1) {
                throw new LikeException(ResultEnum.LIKE_REPEAT_ERROR);
            }
        }else {
            if ((int) redisLikeService.getLikedValue(likedUserId, likedPostId, type) == 1) {
                throw new LikeException(ResultEnum.LIKE_REPEAT_ERROR);
            }
        }

        redisLikeService.saveLiked2Redis(likedUserId,likedPostId,type);
        redisLikeService.incrementLikedCount(likedPostId,type);
    }

    @Transactional
    public void unlike(String likedUserId,String likedPostId,String type) throws LikeException{
        if(!type.equals(LikedService.TYPE_SHARE)&&!type.equals(LikedService.TYPE_HIDE)){
            throw new LikeException(ResultEnum.PARAM_ARTICLE_ERROR);
        }


        if(redisLikeService.getLikedValue(likedUserId,likedPostId,type) == null){
            if(ifLikedByLikedUserIdAndLikedPostId(likedUserId, likedPostId) == 0){
                throw new LikeException(ResultEnum.UNLIKE_REPEAT_ERROR);
            }
        }else{
            if((int)redisLikeService.getLikedValue(likedUserId,likedPostId,type) == 0){
                throw new LikeException(ResultEnum.UNLIKE_REPEAT_ERROR);
            }
        }
        redisLikeService.unlikeFromRedis(likedUserId,likedPostId,type);
        redisLikeService.decrementLikedCount(likedPostId,type);
    }

    @Transactional
    public void transLikedFromRedis2DB(){
        List<Like> list = redisLikeService.getLikedDataFromRedis();

        if(list.size() == 0){
            return;
        }
        for (Like like : list) {
//            log.info("这是like的数据"+like.getLikedPostId()+" "+like.getLikedUserId()+" "+like.getStatus());
            int ul = getByLikedUserIdAndLikedPostId(like.getLikedUserId(), like.getLikedPostId());
//            log.info("这是ul"+ul);
            if (ul == 0){
                //没有记录，直接存入
                save(like);
            }else{
                //有记录，需要更新
                updateLikedStatus(like.getStatus(),like.getLikedPostId(),like.getLikedUserId());
            }
        }
    }

    @Transactional
    public void transLikedCountFromRedis2DB() {
        List<LikedCountDTO> list = redisLikeService.getLikedCountFromRedis();
        if(list.size() == 0){
            return;
        }
        for (LikedCountDTO dto : list) {
            log.info("这是count的数据"+dto.getKey()+" "+dto.getType()+" "+dto.getCount());
            //如果是非匿名文章就更新非匿名
            if(dto.getType().equals(TYPE_SHARE)) {
//                UserShare share = shareService.getShareById(id);
//                  log.info("这是dto"+dto.getCount()+dto.getKey());
                shareService.updateLikeNum(dto.getCount(),dto.getKey());
            }
            //如果是匿名文章就更新匿名
            if(dto.getType().equals(TYPE_HIDE)){
//                UserHide hide = hideService.getHideById(id);
//                  log.info("这是dto"+dto.getCount()+dto.getKey());
                hideService.updateLikeNum(dto.getCount(),dto.getKey());
            }
            }
        }
}
