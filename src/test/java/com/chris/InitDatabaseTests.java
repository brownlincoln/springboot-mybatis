package com.chris;

import com.chris.WendaApplication;
import com.chris.dao.QuestionDAO;
import com.chris.dao.UserDAO;
import com.chris.model.EntityType;
import com.chris.model.Question;
import com.chris.model.User;
import com.chris.service.FollowService;
import com.chris.util.JedisAdapter;
import com.chris.util.WendaUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class InitDatabaseTests {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Autowired
    private FollowService followService;

    @Autowired
    private QuestionDAO questionDAO;

	@Test
	public void initDatabaseTest() {
        Random rand = new Random();
        jedisAdapter.getJedis().flushDB();

        for(int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("user" + (i + 1));
            user.setSalt(UUID.randomUUID().toString().substring(0, 5));
            user.setPassword(WendaUtils.md5("aaa" + user.getSalt()));
            user.setHeadUrl("www.images.nowcoder.com/head/%dt.png" + rand.nextInt(1000));
            userDAO.addUser(user);

            for(int j = 1; j < i; j++) {
                followService.follow(j, EntityType.ENTITY_USER, i);
            }
//            user.setPassword("xxx");
//            userDAO.updatePassword(user);

            String title = "title" + (i+ 1);
            String content = "This is the content" + (i + 1);
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date.setTime(date.getTime() + 1000 * 3600 * 6 * i);
            String sqlDate = formatter.format(date);
            int userId = rand.nextInt(5) + 1;
            int commentCount = rand.nextInt(30);
            Question question = new Question(title, content, sqlDate, userId, commentCount);
            questionDAO.addQuestion(question);
        }

        User user = userDAO.selectById(1);
        System.out.println(user);

        //userDAO.deleteUser(2);
    }



}
