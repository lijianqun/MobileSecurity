package com.npp.MobileSecurity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.npp.MobileSecurity.ui.CustomDialog;
import com.npp.MobileSecurity.utils.MD5Utils;

public class HomeActivity extends Activity {

	private GridView list_home;
	private Myadapter adapter;
	private SharedPreferences sp;
	private AlertDialog dialog;
	private Intent intent;
	private ImageView anim_rotation;
	private ImageView anim_main;
	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	private Button ok;
	private Button cancel;
	private static String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计",
			"手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private static int[] icos = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		list_home = (GridView) findViewById(R.id.list_home);
		anim_rotation = (ImageView) findViewById(R.id.anim_rotation);
		anim_main = (ImageView) findViewById(R.id.anim_main);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		adapter = new Myadapter();
		list_home.setAdapter(adapter);
		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 8:// 设置中心
					intent = new Intent(HomeActivity.this,
							SettingActivity.class);
					startActivity(intent);
					break;
				case 6:// 缓存清理
					intent = new Intent(HomeActivity.this,
							ClearCacheActivity.class);
					startActivity(intent);
					break;
				case 5:// 病毒查杀
					intent = new Intent(HomeActivity.this, VirusActivity.class);
					startActivity(intent);
					break;
				case 2:// 病毒查杀
					intent = new Intent(HomeActivity.this,
							AppManageActivity.class);
					startActivity(intent);
					break;
				case 3:// 进程管理
					intent = new Intent(HomeActivity.this,
							TaskManageAcivity.class);
					startActivity(intent);
					break;
				case 0:
					ShowLostfindDialog();
					break;

				default:
					break;
				}

			}
		});

		// 旋转动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
				R.anim.av_main_rotation);
		anim_rotation.startAnimation(hyperspaceJumpAnimation);
		// 放大动画
		Animation hyperspaceJump = AnimationUtils.loadAnimation(this,
				R.anim.av_main_bright);
		anim_main.startAnimation(hyperspaceJump);
	}

	protected void ShowLostfindDialog() {
		// TODO Auto-generated method stub
		if (IsSetupPwd()) {// 设置过密码
			ShowEnterDialog();
		} else {
			// 没设置过密码。弹出设置框Dialog
			ShowSetupPwdDialog();
		}

	}

	private void ShowSetupPwdDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		// 自定义一个布局文件
		View view = View.inflate(HomeActivity.this, R.layout.dialog_setup_pwd,
				null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 把这个对话框取消掉
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取出密码
				String password = et_setup_pwd.getText().toString().trim();
				String password_confirm = et_setup_confirm.getText().toString()
						.trim();
				if (TextUtils.isEmpty(password)
						|| TextUtils.isEmpty(password_confirm)) {
					Toast.makeText(HomeActivity.this, "密码为空", 0).show();
					return;
				}
				// 判断是否一致才去保存
				if (password.equals(password_confirm)) {
					// 一致的话，就保存密码，把对话框消掉，还要进入手机防盗页面
					Editor editor = sp.edit();
					editor.putString("password", MD5Utils.md5Password(password));// 保存加密后的
					editor.commit();
					dialog.dismiss();
					Intent intent = new Intent(HomeActivity.this,
							LostFindActivity.class);
					startActivity(intent);
				} else {

					Toast.makeText(HomeActivity.this, "密码不一致", 0).show();
					return;
				}

			}
		});
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

	}

	private void ShowEnterDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		// 自定义一个布局文件
		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_enter_password, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取出密码
				String password = et_setup_pwd.getText().toString().trim();
				String savePassword = sp.getString("password", "");// 取出加密后的
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(HomeActivity.this, "密码为空", 1).show();
					return;
				}

				if (MD5Utils.md5Password(password).equals(savePassword)) {
					// 输入的密码是我之前设置的密码
					// 把对话框消掉，进入主页面；
					dialog.dismiss();

					Intent intent = new Intent(HomeActivity.this,
							LostFindActivity.class);
					startActivity(intent);

				} else {
					Toast.makeText(HomeActivity.this, "密码错误", 1).show();
					et_setup_pwd.setText("");
					return;
				}

			}
		});
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 判断是否设置过密码
	 * 
	 * @return
	 */
	private boolean IsSetupPwd() {
		String pwd = sp.getString("password", null);
		return !TextUtils.isEmpty(pwd);
	}

	private class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(HomeActivity.this,
					R.layout.list_home_item, null);
			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			iv_item.setImageResource(icos[position]);
			tv_item.setText(names[position]);
			return view;
		}

	}

}
