package com.chris.controller;

import com.chris.async.EventModel;
import com.chris.async.EventProducer;
import com.chris.async.EventType;
import com.chris.model.*;
import com.chris.service.CommentService;
import com.chris.service.FollowService;
import com.chris.service.QuestionService;
import com.chris.service.UserService;
import com.chris.util.WendaUtils;
import org.apache.catalina.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YaoQi on 2017/2/25.
 */
@Controller
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FollowService followService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "/followUser", method = RequestMethod.POST)
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        User curUser = hostHolder.getUser();
        if (curUser == null) {
            return WendaUtils.getJSONString(999);
        }
        boolean ret = followService.follow(curUser.getId(), EntityType.ENTITY_USER, userId);
        //fire follow event
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(curUser.getId()).setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId).setEntityOwnerId(userId));
        //返回该用户关注的总人数
        return WendaUtils.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(curUser.getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = "/unfollowUser", method = RequestMethod.POST)
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId) {
        User curUser = hostHolder.getUser();
        if (curUser == null) {
            return WendaUtils.getJSONString(999);
        }
        boolean ret = followService.unfollow(curUser.getId(), EntityType.ENTITY_USER, userId);
        //fire follow event
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(curUser.getId()).setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId).setEntityOwnerId(userId));
        //返回该用户关注的总人数
        return WendaUtils.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(curUser.getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = "/followQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        User curUser = hostHolder.getUser();
        if (curUser == null) {
            return WendaUtils.getJSONString(999);
        }
        //if question exists
        Question question = questionService.selectById(questionId);
        if (question == null) {
            return WendaUtils.getJSONString(1, "问题不存在");
        }
        boolean ret = followService.follow(curUser.getId(), EntityType.ENTITY_QUESTION, questionId);
        //fire follow question event
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(curUser.getId()).setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityId(questionId).setEntityOwnerId(question.getUserId()));
        //将该用户的信息放到问题下面
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", curUser.getHeadUrl());
        info.put("name", curUser.getName());
        info.put("id", curUser.getId());
        //得到该问题新的关注数量
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return WendaUtils.getJSONString(ret ? 0 : 1, info);
    }


    @RequestMapping(path = "/unfollowQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        User curUser = hostHolder.getUser();
        if (curUser == null) {
            return WendaUtils.getJSONString(999);
        }
        //if question exists
        Question question = questionService.selectById(questionId);
        if (question == null) {
            return WendaUtils.getJSONString(1, "问题不存在");
        }
        boolean ret = followService.unfollow(curUser.getId(), EntityType.ENTITY_QUESTION, questionId);
        //fire unfollow question event
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(curUser.getId()).setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityId(questionId).setEntityOwnerId(question.getUserId()));
        Map<String, Object> info = new HashMap<>();
        info.put("id", curUser.getId());
        //得到该问题新的关注数量
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return WendaUtils.getJSONString(ret ? 0 : 1, info);
    }
    //未登录用户也可以查看某用户主页
    @RequestMapping(path = "/user/{uid}/followees", method = RequestMethod.GET)
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);
        //正在查看的用户是否登录
        User curUser = hostHolder.getUser();
        //添加某用户关注的用户的详细信息
        if (curUser == null) {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(curUser.getId(), followeeIds));
        }
        //添加某用户关注的用户人数
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        //添加某用户的信息
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    //未登录用户也可以查看某用户主页
    @RequestMapping(path = "/user/{uid}/followers", method = RequestMethod.GET)
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId,0, 10);
        //正在查看的用户是否登录
        User curUser = hostHolder.getUser();
        //添加某用户被关注的人的详细信息
        if (curUser == null) {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(curUser.getId(), followerIds));
        }
        //添加某用户被多少人关注
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        //添加某用户的信息
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            //如果用户不存在，则不作处理
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getCommentCount(EntityType.ENTITY_USER, uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            //如果该用户没有登录，localUserId传入0
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }



}
