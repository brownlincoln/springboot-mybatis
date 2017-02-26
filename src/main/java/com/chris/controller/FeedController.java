package com.chris.controller;

import com.chris.model.EntityType;
import com.chris.model.Feed;
import com.chris.model.HostHolder;
import com.chris.model.User;
import com.chris.service.FeedService;
import com.chris.service.FollowService;
import com.chris.util.JedisAdapter;
import com.chris.util.RedisKeyUtil;
import com.chris.util.WendaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YaoQi on 2017/2/26.
 */
@Controller
public class FeedController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private FeedService feedService;
    @Autowired
    private FollowService followService;

    @Autowired
    private JedisAdapter jedisAdapter;

    //拉模式
    @RequestMapping(path = "/pullfeeds", method = {RequestMethod.GET, RequestMethod.POST})
    public String getPullFeeds(Model model) {
        //user id 0为匿名用户
        int localUserId = hostHolder.getUser() == null ? WendaUtils.ANONYMOUS_USERID: hostHolder.getUser().getId();
        List<Integer> followeeIds = null;
        //登录的用户
        if (localUserId != WendaUtils.ANONYMOUS_USERID) {
            //获得关注的人的信息
            followeeIds = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        //根据用户关注的人的id，到feed数据库中中寻找
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followeeIds, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    //推模式
    @RequestMapping(path = "/pushfeeds", method = {RequestMethod.GET, RequestMethod.POST})
    public String getPushFeeds(Model model) {
        //user id匿名用户，赋予一个id
        int localUserId = hostHolder.getUser() == null ? WendaUtils.ANONYMOUS_USERID: hostHolder.getUser().getId();
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);

        //根据feedId到数据库feed中查找内容
        List<Feed> feeds = new ArrayList<>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }

        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
