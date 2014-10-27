package com.npp.MobileSecurity;

import android.app.Activity;

import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;

public class ShareActivity extends Activity {
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory.getUMSocialService(
			"com.umeng.share", RequestType.SOCIAL);

	protected void onCreate(android.os.Bundle savedInstanceState) {
		// setContentView(layoutResID);
		// 设置分享内容
		mController.setShareContent("的撒娇和大家撒谎的家啊还是觉得卡");
		// 设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(this,
				"http://www.umeng.com/images/pic/banner_module_social.png"));
	};

	// 设置分享图片，参数2为本地图片的资源引用
	// mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
	// 设置分享图片，参数2为本地图片的路径(绝对路径)
	// mController.setShareMedia(new UMImage(getActivity(),
	// BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

	// 设置分享音乐
	// UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
	// uMusic.setAuthor("GuGu");
	// uMusic.setTitle("天籁之音");
	// 设置音乐缩略图
	// uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
	// mController.setShareMedia(uMusic);

	// 设置分享视频
	// UMVideo umVideo = new UMVideo(
	// "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
	// 设置视频缩略图
	// umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
	// umVideo.setTitle("友盟社会化分享!");
	// mController.setShareMedia(umVideo);

}
