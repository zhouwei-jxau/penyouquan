package com.example.zhouwei.comments;

/**
 * Created by zhouwei on 2018/12/13.
 */

public class CurrentUser {
    static User user=null;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        CurrentUser.user = user;
    }
}
