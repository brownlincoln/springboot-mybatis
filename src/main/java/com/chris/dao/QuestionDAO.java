package com.chris.dao;

import com.chris.model.Question;
import javafx.scene.control.Tab;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by YaoQi on 2017/2/18.
 */
@Repository
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELD = " title, content, createdDate, userId, commentCount ";
    String SELECT_FIELD = " id, " + INSERT_FIELD;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELD, ") values (" +
            "#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question question);


    List<Question> selectLatestQuestions(
            @Param("userId") int userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Select({"select", SELECT_FIELD, "from ", TABLE_NAME, "where id = #{qid}"})
    public Question selectById(int qid);

    @Update({"update ", TABLE_NAME, "set commentCount = #{commentCount} where id = #{id}"})
    public int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);
}
