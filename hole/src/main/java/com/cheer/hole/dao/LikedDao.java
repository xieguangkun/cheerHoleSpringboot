package com.cheer.hole.dao;

import com.cheer.hole.entity.Like;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedDao {
    /**
     * 增加一条点赞记录
     * @param like
     * @return
     */
    int insertLike(@Param("like")Like like);

    /**
     * 更新数据库中点赞记录的状态，如果为0是没点赞，如果是1就是点赞了
     * @param status
     * @param likedPostId
     * @return
     */
    int updateLikeStatus(@Param("status") int status,@Param("likedPostId")String likedPostId,@Param("likedUserId")String likedUserId);

    /**
     * 查询点赞记录是否存在
     * @param liked_user_id
     * @param liked_post_id
     * @return
     */
    int getByLikedUserIdAndLikedPostId(@Param("likedUserId") String liked_user_id,@Param("likedPostId") String liked_post_id);

    /**
     * 从数据库查询该条记录是否被赞了，与上面的接口不同的是上面是查询字段是否存在从而加入数据库的，
     * 这是判断是否被赞的
     * @param liked_user_id
     * @param liked_post_id
     * @return
     */
    int ifLikedByLikedUserIdAndLikedPostId(@Param("likedUserId") String liked_user_id,@Param("likedPostId") String liked_post_id);

    /**
     * 返回当前用户点赞的所有非匿名文章记录
     * @param likedUserId
     * @return
     */
    List<Like> getMyLikeFromShare(@Param("likedUserId") String likedUserId);

    /**
     * 返回当前用户点赞的所有非匿名文章记录
     * @param likedUserId
     * @return
     */
    List<Like> getMyLikeFromHide(@Param("likedUserId") String likedUserId);


}
