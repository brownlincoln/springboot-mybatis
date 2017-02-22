package com.chris.dao;

import com.chris.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YaoQi on 2017/2/22.
 */
@Repository
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELD = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELD = " id, " + INSERT_FIELD;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELD, ") values (" +
            "#{fromId}, #{toId}, #{content}, #{createdDate}, #{hasRead}, #{conversationId})"})
    public int addMessage(Message message);

    @Select({"select ", SELECT_FIELD, "from", TABLE_NAME, "where conversation_id = " +
            "#{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    public List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit);

    @Select({"select ", INSERT_FIELD, ", count(id) as id from (select * from ", TABLE_NAME, "where from_id = #{userId} or to_id " +
            "= #{userId} order by created_date desc) tt group by conversation_id order by created_date desc" +
            " limit #{offset}, #{limit}"})
    public List<Message> getConversationList(@Param("userId") int userId,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, "where to_id = #{userId}" +
            " and conversation_id = #{conversationId} and has_read = 0"})
    public int getConversationUnreadCount(@Param("userId") int userId,
                                          @Param("conversationId") String conversationId);
}
