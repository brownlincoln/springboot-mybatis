package com.chris.service;

import com.chris.WendaApplication;
import com.chris.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by YaoQi on 2017/2/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void testgetConversationDetail() {
        List<Message> messages = messageService.getConversationDetail("11_14", 0, 10);
        for (Message message : messages) {
            System.out.println(message.getId());
        }
    }


}
