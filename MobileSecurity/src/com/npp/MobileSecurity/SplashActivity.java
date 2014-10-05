package com.npp.MobileSecurity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.npp.MobileSecurity.utils.StreamTools;

public class SplashActivity extends Activity {

	private TextView tv_version;
	protected static final String tag = "SplashActivity";
	protected static final int ENTER_HOME = 0;
	protected static final int SHOW_UPDATE_DIALOG = 1;
	protected static final int JSON_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int URL_ERROR = 4;
	private static final String TAG = "SplashActivity";

	private String description;
	private String apkurl;
	private TextView tv_updata_info;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_acvtivity);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_updata_info = (TextView) findViewById(R.id.tv_updata_info);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tv_version.setText("版本号:" + getVersionName());

		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(1000);
		findViewById(R.id.rl_root_splash).setAnimation(aa);
		boolean isupdate = sp.getBoolean("update", false);
		if (isupdate) {
			// 自动检查更新
			checkupdate();
		} else {
			// 不检查更新延迟2秒进入主界面
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					EnterHome();

				}
			}, 2000);
		}
		copyDB("antivirus.db");
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case ENTER_HOME:// 进入主页面
				Log.i(tag, "进入主页面");
				EnterHome();
				break;
			case SHOW_UPDATE_DIALOG:// 显示升级
				Toast.makeText(getApplicationContext(), "有新版本", 0).show();
				ShowUpdateDialog();
				break;
			case JSON_ERROR:// Json解析错误
				Log.i(tag, "Json解析错误");
				Toast.makeText(getApplicationContext(), "Json解析错误", 0).show();
				EnterHome();
				break;
			case NETWORK_ERROR:// 网络异常
				Log.i(tag, "网络异常");
				Toast.makeText(getApplicationContext(), "网络异常", 0).show();
				EnterHome();
				break;
			case URL_ERROR:// URL错误
				Log.i(tag, "URL错误");
				Toast.makeText(getApplicationContext(), "URL错误", 0).show();
				EnterHome();
				break;
			default:
				break;
			}
		}

	};

	private void EnterHome() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		// 关闭当前页面
		finish();
	}

	protected void ShowUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示升级");
		builder.setMessage(description);
		// builder.setCancelable(false);//强制升级，取消返回按钮和触摸外面区域
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// 点返回进去主页面
				EnterHome();
				dialog.dismiss();

			}
		});
		builder.setPositiveButton("立即升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// SD卡存在
					HttpUtils http = new HttpUtils();
					HttpHandler handler = http.download(apkurl, Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/MobileSecurity.apk", false, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
							true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
							new RequestCallBack<File>() {

								@Override
								public void onStart() {
									// TODO Auto-generated method stub
									super.onStart();
									tv_updata_info.setVisibility(View.VISIBLE);
									tv_updata_info.setText("conn...");
								}

								@Override
								public void onLoading(long total, long current,
										boolean isUploading) {
									// 当前下载百分比
									int progress = (int) (current * 100 / total);
									tv_updata_info.setText("下载进度" + progress
											+ "%");
								}

								@Override
								public void onSuccess(
										ResponseInfo<File> responseInfo) {
									tv_updata_info.setText("downloaded:"
											+ responseInfo.result.getPath());
									installAPK(responseInfo);
								}

								/**
								 * 安装APK
								 * 
								 * @param responseInfo
								 */
								private void installAPK(
										ResponseInfo<File> responseInfo) {
									// TODO Auto-generated method stub
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(
											Uri.fromFile(responseInfo.result),
											"application/vnd.android.package-archive");
									startActivity(intent);

								}

								@Override
								public void onFailure(HttpException error,
										String msg) {
									Toast.makeText(SplashActivity.this,
											"下载失败了", 0).show();
								}
							});
				} else {
					// SD卡不存在
					Toast.makeText(SplashActivity.this, "SD卡不存在，请下次再试", 0)
							.show();
					return;

				}
			}
		});
		builder.setNegativeButton("下次再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				EnterHome();// 进入主页面

			}
		});
		builder.show();

		new task().execute();

	}

	class task extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}

	private void checkupdate() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {

				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();
				URL url;
				try {
					url = new URL(getString(R.string.serviceurl));
					HttpURLConnection con = (HttpURLConnection) url
							.openConnection();
					con.setConnectTimeout(4000);
					con.setRequestMethod("GET");
					int ResponseCode = con.getResponseCode();
					if (ResponseCode == 200) {
						InputStream is = con.getInputStream();
						String result = StreamTools.readFromStream(is);
						Log.i(tag, "联网成功" + result);
						// 解析json,得到服务器的版本信息
						JSONObject obj = new JSONObject(result);
						String version = (String) obj.get("version");
						description = (String) obj.get("description");
						apkurl = (String) obj.get("apkurl");

						// 校验是否有新版本
						if (getVersionName().equals(version)) {
							// 版本一致，没有新版本进图主页面
							msg.what = ENTER_HOME;
						} else {
							// 有新版本，弹出对话框
							msg.what = SHOW_UPDATE_DIALOG;
						}
					} else {
						Log.i(tag, "联网失败");
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = NETWORK_ERROR;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = JSON_ERROR;
				} finally {
					long endTime = System.currentTimeMillis();
					// 我们花了多少时间
					long dTime = endTime - startTime;
					// 2000
					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
				}

			}
		}.start();

	}

	/**
	 * 获取程序版本号
	 */
	private String getVersionName() {
		// 用来管理手机的APK
		PackageManager pm = getPackageManager();
		try {
			// 得到知道APK的功能清单文件
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * //path 把address.db这个数据库拷贝到data/data/《包名》/files/address.db
	 */
	private void copyDB(String filename) {
		// 只要你拷贝了一次，我就不要你再拷贝了
		try {
			File file = new File(getFilesDir(), filename);
			if (file.exists() && file.length() > 0) {
				// 正常了，就不需要拷贝了
				Log.i(TAG, "正常了，就不需要拷贝了");
			} else {
				InputStream is = getAssets().open(filename);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
