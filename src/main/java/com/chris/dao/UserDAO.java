package com.chris.dao;

import com.chris.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created by LJFENG on 2017/2/17 0017.
 */
@Repository
@Mapper
public interface UserDAO {
    String TABLE_NAME = " user ";
    String INSERT_FIELD = " name, password, salt, head_url ";
    String SELECT_FIELD = " id, " + INSERT_FIELD;

    @Insert({"insert into ", TABLE_NAME , "(", INSERT_FIELD, " ) values" +
            "(#{name}, #{password}, #{salt}, #{headUrl})"})
    public int addUser(User user);

    @Select({"select ", SELECT_FIELD, "from ", TABLE_NAME, " where id = #{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELD, "from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    @Update({"update ", TABLE_NAME, "set password=#{password} where id = #{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id = #{id}"})
    void deleteUser(int id);
}
