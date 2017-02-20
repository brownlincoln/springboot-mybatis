package com.chris.service;

import com.chris.WendaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by YaoQi on 2017/2/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class AddLoginTicketTest {

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        userService.addLoginTicket(1);
    }
}
