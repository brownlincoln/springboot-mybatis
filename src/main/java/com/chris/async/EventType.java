package com.chris.async;

/**
 * Created by YaoQi on 2017/2/24.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5);

    private int value;

    private EventType(int value) {
        this.value = value;
    }
    public int getValue() {
        return this.value;
    }
}
