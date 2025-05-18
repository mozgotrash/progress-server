package org.mozgotrash.tg;

import org.mozgotrash.constant.BotState;

import java.util.HashMap;
import java.util.Map;

public class UserStateManager {
    private final Map<Long, BotState> userState = new HashMap<>();
    private final Map<Long, Long> userBookId = new HashMap<>();

    public BotState getState(Long userId) {
        return userState.getOrDefault(userId, BotState.START);
    }

    public void setState(Long userId, BotState state) {
        userState.put(userId, state);
    }

    public Long getBookId(Long userId) {
        return userBookId.get(userId);
    }

    public void setUserBookId(Long userId, Long bookId) {
        userBookId.put(userId, bookId);
    }
}
