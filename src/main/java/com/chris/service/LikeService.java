package com.chris.service;

import com.chris.util.JedisAdapter;
import com.chris.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by YaoQi on 2017/2/23.
 */
@Service
public class LikeService {
    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);
    @Autowired
    private JedisAdapter jedisAdapter;

    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
//        logger.info(likeKey);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

        //点击喜欢的用户，还需要从dislike那一部分中移除，如果他之前不喜欢的话
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    public long dislike(int userId, int entityType, int entityId) {
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.sadd(dislikeKey, String.valueOf(userId));

        //点击不喜欢的用户，还需要从like那一部分中移除，如果他之前喜欢的话
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        //返回的都是likeKey中value的个数
        return jedisAdapter.scard(likeKey);
    }

    //如果喜欢，返回1；不喜欢返回-1；无表示，返回0。
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        return jedisAdapter.sismember(dislikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    //返回喜欢状态的数量
    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }

}
