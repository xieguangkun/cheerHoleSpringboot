package com.cheer.hole.dao;

import com.cheer.hole.entity.UserShare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserShareDao {
    /**
     * 新增文章
     * @param avatar
     * @param text
     * @param user_name
     * @param open_id
     * @return
     */
    int insertUserShare(@Param("uuid")String id,@Param("avatar")String avatar,@Param("text")String text,@Param("userName")String user_name,@Param("openId")String open_id);

    /**
     * 获取所有的文章
     * @param pageNum
     * @return
     */
    List<UserShare> getAllShare(@Param("pageNum")int pageNum);

    /**
     * 跟据id获取文章
     * @param id
     * @return
     */
    UserShare getShareById(@Param("id") String id);

    /**
     * 获取我发送的文章
     * @param openId
     * @return
     */
    List<UserShare> getMyShare(@Param("openId") String openId);

    /**
     * 发送一条文章
     * @param share
     * @return
     */
    int addShare(UserShare share);

    /**
     * 跟据id删除文章
     * @param id
     * @return
     */
    int deleteShare(@Param("id") String id);

    /**
     * 改变文章评论的数量
     * @param likeNum
     * @param likedPostId
     * @return
     */
    int updateLikeNum(@Param("likeNum") int likeNum,@Param("likedPostId") String likedPostId);

    /**
     * 评论数+1
     * @param commPostId
     * @return
     */
    int decrCommNum(@Param("commPostId") String commPostId);

    /**
     * 评论数-1
     * @param commPostId
     * @return
     */
    int incrCommNum(@Param("commPostId") String commPostId);

    int updateLookedNum(@Param("lookedNum")int lookedNum,@Param("lookedPostId")String lookedPostId);
}
