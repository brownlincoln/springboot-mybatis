package com.chris.dao;

import com.chris.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YaoQi on 2017/2/26.
 */
@Repository
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELD = " type, user_id, created_date, data ";
    String SELECT_FIELD = " id, " + INSERT_FIELD;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELD, ") values (" +
            "#{type}, #{userId}, #{createdDate}, #{data})"})
    int addFeed(Feed feed);

    @Select({"select", SELECT_FIELD, "from", TABLE_NAME, "where id = #{id}"})
    Feed getFeedById(@Param("id") int id);


    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);
}
