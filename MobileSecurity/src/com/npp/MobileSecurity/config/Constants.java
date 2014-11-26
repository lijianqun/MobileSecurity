package com.npp.MobileSecurity.config;

/**
 * 
 * @author NingPingPing
 * 
 */
public interface Constants {
    /**
     * 当前APP的Key
     */
    public static final String APP_KEY = "2189520315";

    /**
     * 回调页面
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
}
