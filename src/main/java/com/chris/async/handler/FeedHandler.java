package com.chris.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.chris.async.EventHandler;
import com.chris.async.EventModel;
import com.chris.async.EventType;
import com.chris.model.EntityType;
import com.chris.model.Feed;
import com.chris.model.Question;
import com.chris.model.User;
import com.chris.service.FeedService;
import com.chris.service.FollowService;
import com.chris.service.QuestionService;
import com.chris.service.UserService;
import com.chris.util.JedisAdapter;
import com.chris.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by YaoQi on 2017/2/26.
 */
public class FeedHandler implements EventHandler{
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private FollowService followService;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public void doHandle(EventModel model) {
        Feed feed = new Feed();
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setCreatedDate(new Date());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            //不支持的feed
            return;
        }
        feedService.addFeed(feed);

        //推模式,用了redis来存储feed的id
        //获得所有粉丝
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        //系统队列？？？
        followerIds.add(0);
        //给所有粉丝推事件
        for (Integer followerId : followerIds) {
            //每个粉丝都有自己的key，里面放着订阅的followee的内容id
            String timelineKey = RedisKeyUtil.getTimelineKey(followerId);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
        }
    }

    private String buildFeedData(EventModel model) {
        Map<String, String> map  = new HashMap<>();
        User actor = userService.getUser(model.getActorId());
        //如果用户不存在，直接返回null
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userName", actor.getName());
        map.put("userHead", actor.getHeadUrl());

        //关注问题，或者是评论就会触发事件
        if(model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionService.selectById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT, EventType.FOLLOW);
    }
}
