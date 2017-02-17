package com.chris;

import com.chris.WendaApplication;
import com.chris.dao.UserDAO;
import com.chris.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class InitDatabaseTests {

    @Autowired
    private UserDAO userDAO;

	@Test
	public void initDatabaseTest() {
        Random rand = new Random();

        for(int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("user" + i);
            user.setPassword("password" + i);
            user.setSalt("salt" + i);
            user.setHeadUrl("www.img.nowcoder.com/" + rand.nextInt(1000));
            userDAO.addUser(user);
        }


    }

}
