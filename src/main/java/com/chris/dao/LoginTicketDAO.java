package com.chris.dao;

import com.chris.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created by YaoQi on 2017/2/19.
 */
@Repository
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELD = " user_id, ticket, expired, status ";
    String SELECT_FIELD = " id, " + INSERT_FIELD;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELD, ") values " +
            "( #{userId},  #{ticket}, #{expired}, #{status})"})
    public int addTicket(@Param("ticket") LoginTicket loginTicket);

    @Select({"select ", SELECT_FIELD, "from ", TABLE_NAME, "where ticket=#{ticket}"})
    public LoginTicket selectByTicket(String ticket);

    @Update({"update ", TABLE_NAME, "set status=#{status} where ticket=#{ticket}"})
    public void updateStatus(String ticket, int status);
}
