package com.chris.util;

/**
 * Created by YaoQi on 2017/2/23.
 */
public class RedisKeyUtil {
    public static final String SPLIT = ":";
    public static final String BIZ_LIKE = "LIKE";
    public static final String BIZ_DISLIKE = "DISLIKE";
    public static final String BIZ_EVENTQUEUE = "EVENT_QUEUE";

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }
}
