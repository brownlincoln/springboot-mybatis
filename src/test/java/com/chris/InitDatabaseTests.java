package com.chris;

import com.chris.WendaApplication;
import com.chris.dao.QuestionDAO;
import com.chris.dao.UserDAO;
import com.chris.model.EntityType;
import com.chris.model.Question;
import com.chris.model.User;
import com.chris.service.FollowService;
import com.chris.util.JedisAdapter;
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
            user.setName("user" + i);
            user.setPassword("");
            user.setSalt("");
            user.setHeadUrl("www.images.nowcoder.com/head/%dt.png" + rand.nextInt(1000));
            userDAO.addUser(user);

            for(int j = 1; j < i; j++) {
                followService.follow(j, EntityType.ENTITY_USER, i);
            }
//            user.setPassword("xxx");
//            userDAO.updatePassword(user);

            String title = "title" + i;
            String content = "StringUtils.isBlank() checks that each character of the string is a whitespace " +
                    "character (or that the string is empty or that it's null). This is totally different than just" +
                    " checking if the string is empty.";
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date.setTime(date.getTime() + 1000 * 3600 * 6 * i);
            String sqlDate = formatter.format(date);
            int userId = rand.nextInt(3) + 1;
            int commentCount = rand.nextInt(30);
            Question question = new Question(title, content, sqlDate, userId, commentCount);
            questionDAO.addQuestion(question);
        }

        User user = userDAO.selectById(1);
        System.out.println(user);

        //userDAO.deleteUser(2);
    }



}
