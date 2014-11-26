package com.npp.MobileSecurity.WB;

import android.app.Activity;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 * 
 * @author NingPingPing
 * 
 */
public class WBAuthActivity extends Activity {

    /** 微博 Web 授权类，提供登陆等功能 */
    private WeiboAuth mWeiboAuth;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

}
