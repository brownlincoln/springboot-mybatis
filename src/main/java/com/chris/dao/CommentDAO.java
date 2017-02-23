package com.chris.dao;

import com.chris.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YaoQi on 2017/2/22.
 */
@Repository
public interface CommentDAO {

    String TABLE_NAME = " comment ";
    String INSERT_FIELD = " content, user_id, entity_id, entity_type, created_date, status ";
    String SELECT_FIELD = " id, " + INSERT_FIELD;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELD, ") values (" +
            "#{content}, #{userId}, #{entityId}, #{entityType}, #{createdDate}, #{status})"})
    public int addComment(Comment comment);

    @Select({"select ", SELECT_FIELD, "from ", TABLE_NAME, "where entity_id = #{entityId}" +
            " and entity_type = #{entityType} order by created_date desc limit #{offset}, #{limit}"})
    public List<Comment> selectCommentByEntity(@Param("entityId") int entityId,
                                        @Param("entityType") int entityType,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select({"select ", SELECT_FIELD, "from ", TABLE_NAME, "where id = #{commentId}"})
    public Comment getCommentById(@Param("commentId") int commentId);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id = #{entityId} and entity_type = #{entityType}" })
    public int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update ", TABLE_NAME, "set status = #{status} where id = #{id}"})
    public int updateStatus(@Param("id") int id, @Param("status") int status);

}
