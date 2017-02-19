package com.chris.service;

import com.chris.dao.QuestionDAO;
import com.chris.dao.UserDAO;
import com.chris.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by YaoQi on 2017/2/19.
 */
@Service
public class QuestionService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

}
