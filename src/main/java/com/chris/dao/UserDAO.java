package com.chris.dao;

import com.chris.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by LJFENG on 2017/2/17 0017.
 */

@Mapper
public interface UserDAO {
    String TABLE_NAME = " user ";
    String INSERT_FIELD = " name, password, salt, head_url ";
    String SELECT_FIELD = " id, " + INSERT_FIELD;

    @Insert({"insert into ", TABLE_NAME , "(", INSERT_FIELD, " ) values" +
            "(#{name}, #{password}, #{salt}, #{headUrl})"})
    public int addUser(User user);


}
