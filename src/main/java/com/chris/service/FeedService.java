package com.chris.service;

import com.chris.dao.FeedDAO;
import com.chris.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by YaoQi on 2017/2/26.
 */
@Service
public class FeedService {
    @Autowired
    private FeedDAO feedDAO;

    //拉模式
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    public boolean addFeed(Feed feed) {
        return feedDAO.addFeed(feed) > 0;
    }

    //推模式
    public Feed getById(int id) {
        return feedDAO.getFeedById(id);
    }
}
