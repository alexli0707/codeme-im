package org.codeme.im.imcommon.constant;

public class RedisKeyConstant {
    private static final String PROJECT_PREFIX = "codeme_im:";
    //accesstoken,value:string
    private static final String ACCESS_TOKEN = PROJECT_PREFIX + "access_token:%s";
    //refreshtoken,value:string
    private static final String REFRESH_TOKEN = PROJECT_PREFIX + "refresh_token:%s";

    private static final String CHATROOM_MEMBERS = PROJECT_PREFIX + "chatroom:%d";

    private static final String USER_SOCKET_BELONG = PROJECT_PREFIX + "user:%d";


    public static String getAccessTokenKey(String accessToken) {
        return String.format(ACCESS_TOKEN, accessToken);
    }

    public static String getRefreshTokenKey(String refreshToken) {
        return String.format(REFRESH_TOKEN, refreshToken);
    }

    public static String getChatroomMembers(Long chatroomId) {
        return String.format(CHATROOM_MEMBERS, chatroomId);
    }

    public static String getUserSocketBelong(Long userId) {
        return String.format(USER_SOCKET_BELONG, userId);
    }
}
