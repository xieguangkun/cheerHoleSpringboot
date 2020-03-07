package com.cheer.hole.dao;

import com.cheer.hole.entity.UserHide;
import com.cheer.hole.entity.UserShare;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHideDao {
    /**
     * 获取所有的匿名文章
     * @param pageNum
     * @return
     */
    List<UserHide> getHideByEmotion(@Param("emotion")String emotion,@Param("pageNum")int pageNum);

    /**
     * 跟据id查匿名文章
     * @param id
     * @return
     */
    UserHide getHideById(@Param("id") String id);

    /**
     * 获取我发送的匿名文章
     * @param openId
     * @return
     */
    List<UserHide> getMyHide(@Param("openId") String openId);

    /**
     * 新增一条匿名
     * @param id
     * @param avatar
     * @param text
     * @param user_name
     * @param open_id
     * @param type
     * @return
     */
    int insertUserHide(@Param("uuid")String id,@Param("avatar")String avatar,@Param("text")String text,@Param("userName")String user_name,@Param("openId")String open_id,@Param("type")String type);

    /**
     * 删除一条匿名文章
     * @param id
     * @return
     */
    int deleteHide(@Param("id")String id);

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

    /**
     * 设置浏览次数
     * @param lookedNum
     * @param lookedPostId
     * @return
     */
    int updateLookedNum(@Param("lookedNum")int lookedNum,@Param("lookedPostId")String lookedPostId);
}
