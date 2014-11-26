package com.npp.MobileSecurity;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.List;

import com.npp.MobileSecurity.db.dao.VirusDao;

import android.R.color;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

public class VirusActivity extends Activity {
	protected static final int SCAN = 0;
	protected static final int FINISH = 1;
	private ImageView iv_scan;
	private ProgressBar progressBar1;
	private PackageManager pm;
	private TextView tv_scan_status;
	private LinearLayout ll_container;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN:
				Scaninfo info = (Scaninfo) msg.obj;
				tv_scan_status.setText("正在扫描：" + info.name);

				TextView tv_scaninfo = new TextView(getApplicationContext());

				if (info.isVirus) {
					tv_scaninfo.setTextColor(Color.RED);
					tv_scaninfo.setText("发现病毒：" + info.name);
				} else {
					tv_scaninfo.setTextColor(Color.BLACK);
					tv_scaninfo.setText("扫描安全： " + info.name);
				}

				ll_container.addView(tv_scaninfo, 0);
				break;
			case FINISH:
				tv_scan_status.setText("扫描完毕");
				iv_scan.clearAnimation();// 暂停动画
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_virus);
		
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		RotateAnimation ra = new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);
		iv_scan.startAnimation(ra);
		ScanVirus();
	}

	/**
	 * 扫描病毒
	 */
	private void ScanVirus() {
		// TODO Auto-generated method stub
		pm = getPackageManager();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<PackageInfo> Packageinfos = pm.getInstalledPackages(0);
				int progress = 0;
				progressBar1.setMax(Packageinfos.size());
				for (PackageInfo packageinfo : Packageinfos) {
					String path = packageinfo.applicationInfo.sourceDir;
					String md5 = getFileMd5(path);// 得到安装程序文件的Md5值
					Scaninfo sancinfo = new Scaninfo();
					sancinfo.packname = packageinfo.applicationInfo.packageName;
					sancinfo.name = packageinfo.applicationInfo.loadLabel(pm)
							.toString();
					if (VirusDao.IsVirus(md5))
						// 发现病毒
						sancinfo.isVirus = true;
					else {
						sancinfo.isVirus = false;
					}
					Message msg = new Message();
					msg.obj = sancinfo;
					msg.what = SCAN;
					handler.sendMessage(msg);
					progress++;
					progressBar1.setProgress(progress);

				}
				Message msg = Message.obtain();
				msg.what = FINISH;
				handler.sendMessage(msg);
			};
		}).start();

	}

	/**
	 * 应用名 包名 是否为病毒
	 */
	class Scaninfo {
		String name;
		String packname;
		boolean isVirus;
	}

	/**
	 * 获取文件的md5值
	 * 
	 * @param path
	 *            文件的全路径名称
	 * @return
	 */
	private String getFileMd5(String path) {
		try {
			// 获取一个文件的特征信息，签名信息。
			File file = new File(path);
			// md5
			MessageDigest digest = MessageDigest.getInstance("md5");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			byte[] result = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : result) {
				// 与运算
				int number = b & 0xff;// 加盐
				String str = Integer.toHexString(number);
				// System.out.println(str);
				if (str.length() == 1) {
					sb.append("0");
				}
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
