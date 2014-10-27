package com.npp.MobileSecurity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.npp.MobileSecurity.ui.SettingItemView;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;

public class SettingActivity extends Activity {
	private SettingItemView siv_update;
	private SharedPreferences sp;
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
            RequestType.SOCIAL);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean update = sp.getBoolean("update", false);
		if (update) {
			siv_update.setChecked(true);

		} else {
			siv_update.setChecked(false);
		}

		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				// TODO Auto-generated method stub
				if (siv_update.isChecked()) {// 已经打开自动升级
					siv_update.setChecked(false);
					editor.putBoolean("update", false);
				} else {// 没有打开自动升级
					siv_update.setChecked(true);
					editor.putBoolean("update", true);
				}
				editor.commit();

			}
		});
	}
	
	public void share(View view)
	{
		//设置分享内容
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		//设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(getActivity(), 
		"http://www.umeng.com/images/pic/banner_module_social.png"));
		//为了保证人人分享成功且能够在PC上正常显示，请设置website                                      
		mController.setAppWebSite(SHARE_MEDIA.RENREN, "http://www.umeng.com/social");
		//设置分享图片，参数2为本地图片的资源引用
		//mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
		//设置分享图片，参数2为本地图片的路径(绝对路径)
		//mController.setShareMedia(new UMImage(getActivity(), 
		//BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

		//设置分享音乐
		//UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
		//uMusic.setAuthor("GuGu");
		//uMusic.setTitle("天籁之音");
		//设置音乐缩略图
		//uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		//mController.setShareMedia(uMusic);

		//设置分享视频
		//UMVideo umVideo = new UMVideo(
		//"http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
		//设置视频缩略图
		//umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		//umVideo.setTitle("友盟社会化分享!");
		//mController.setShareMedia(umVideo);
	}


}
