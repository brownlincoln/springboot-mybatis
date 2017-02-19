package com.chris;

import com.chris.dao.QuestionDAO;
import com.chris.dao.UserDAO;
import com.chris.model.Question;
import com.chris.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by YaoQi on 2017/2/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class QuestionDAOTest {
    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private UserDAO userDAO;

    @Test
    public void test() {
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("user" + i);
            user.setPassword("password" + i);
            user.setSalt("salt" + i);
            user.setHeadUrl(String.format("www.images.nowcoder.com/head/%dt.png", rand.nextInt(1000)));

            userDAO.addUser(user);
           // System.out.println(user.getId());
            String title = "title" + i;
            String content = "content" + i;
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date.setTime(date.getTime() + 1000 * 3600 * 6 * i);
            String sqlDate = formatter.format(date);
            int userId = rand.nextInt(3) + 1;
            int commentCount = rand.nextInt(30);
            Question question = new Question(title, content, sqlDate, userId, commentCount);
            questionDAO.addQuestion(question);

        }

    }
}
