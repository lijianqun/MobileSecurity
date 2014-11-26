package com.npp.MobileSecurity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.npp.MobileSecurity.adapter.myViewHolder;
import com.npp.MobileSecurity.adapter.ViewPagerAdapter;
import com.npp.MobileSecurity.domain.AppInfo;
import com.npp.MobileSecurity.engine.AppInfoProvider;

public class AppManageActivity extends Activity {
	private TextView tv_appcount;
	private TextView tv_SDorROMsize;
	private ViewPager viewPager;
	private ListView ll_userapp_list;
	private ListView ll_Sysapp_list;
	private ViewPagerAdapter viewPagerAdapter;
	private AppInfo appInfo;
	long RomSize;
	long SDSize;
	int courrt;
	private LinearLayout nav_layout;
	private TextView[] nav;
	/**
	 * 用户应用程序的集合
	 */
	private List<AppInfo> userAppInfos;

	/**
	 * 系统应用程序的集合
	 */
	private List<AppInfo> systemAppInfos;

	/**
	 * 获取所有应用程序序信息
	 */
	private List<AppInfo> appinfos;

	private List<View> list_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanage);
		tv_appcount = (TextView) findViewById(R.id.tv_ROMsize);
		tv_SDorROMsize = (TextView) findViewById(R.id.tv_Sdsize);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		nav_layout = (LinearLayout) findViewById(R.id.nav_layout);
		// 获得可用内存
		RomSize = getPathAvlibeSpace(Environment.getDataDirectory()
				.getAbsolutePath());
		// 获得可用外存
		SDSize = getPathAvlibeSpace(Environment
				.getExternalStorageDirectory().getAbsolutePath());


		// 获取
		int count = nav_layout.getChildCount();
		nav = new TextView[count];
		for (int i = 0; i < count; i++) {
			nav[i] = (TextView) nav_layout.getChildAt(i);
			nav[i].setOnClickListener(myOnclickListener);
			nav[i].setTag(i);
		}

		SortApp();

		ll_userapp_list = new ListView(this);
		ll_Sysapp_list = new ListView(this);
		list_view = new ArrayList<View>();
		list_view.add(ll_userapp_list);
		list_view.add(ll_Sysapp_list);
		viewPagerAdapter = new ViewPagerAdapter(list_view);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(new MyPageChangListener());
		tv_SDorROMsize.setText("剩余SD卡:" + Formatter.formatFileSize(this, SDSize));
	}

	/**
	 * 分离出系统应用和用户应用
	 */
	public void SortApp() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 加载包信息
				appinfos = AppInfoProvider.getAppInfos(AppManageActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for (AppInfo info : appinfos) {
					if (info.isUserApp()) {			
						userAppInfos.add(info);
					} else {

						systemAppInfos.add(info);
					}
				}			
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ll_userapp_list.setAdapter(new myuserBaseAdapter());
						ll_Sysapp_list.setAdapter(new mysysBaseAdapter());
						tv_appcount.setText("已安装软件: " + userAppInfos.size());
					}
				});
			}
		}).start();
	}

	/**
	 * 获取整个内存大小=区块的个数*区块的大小 获取可用内存大小=可用区块的个数*区块的大小
	 * 
	 * @param path
	 * @return
	 */
	public long getPathAvlibeSpace(String path) {
		StatFs sf = new StatFs(path);
		sf.getBlockCount();// 获取区块的个数
		long size = sf.getBlockSize();// 获取区块的大小
		long count = sf.getAvailableBlocks();// 获取可用的区块个数
		return size * count;
	}

	/**
	 * 导航条点击事件
	 */
	private OnClickListener myOnclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setClickChange((Integer) v.getTag());
			viewPager.setCurrentItem((Integer) v.getTag());

		}
	};

	private void setClickChange(int index) {
		for (TextView tv : nav) {
			tv.setEnabled(true);
		}
		nav[index].setEnabled(false);
		if(index==0)
		{
			tv_appcount.setText("已安装软件: " + userAppInfos.size());
			tv_SDorROMsize.setText("剩余SD卡:" + Formatter.formatFileSize(this, SDSize));
			
		}
		else if(index==1)
		{
			
			tv_appcount.setText("系统软件: " + systemAppInfos.size());
			tv_SDorROMsize.setText("剩余SD卡:" + Formatter.formatFileSize(this, RomSize));
		}
	}

	private class MyPageChangListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int flag) {
			// TODO Auto-generated method stub
			courrt = flag;
			setClickChange(flag);
		}
	}

	private class myuserBaseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userAppInfos.size();

		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return userAppInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				convertView = View.inflate(AppManageActivity.this,
						R.layout.userapp_list_item, null);
			}
			ImageView iv_app_icon = myViewHolder.get(convertView,
					R.id.iv_app_icon);
			TextView tv_app_version = myViewHolder.get(convertView,
					R.id.tv_app_lastUpdate);
			TextView tv_appName = myViewHolder
					.get(convertView, R.id.tv_appName);
			Button btn_uninstall=myViewHolder
					.get(convertView, R.id.btn_uninstall);
			final AppInfo userappInfo = userAppInfos.get(position);
			iv_app_icon.setImageDrawable(userappInfo.getApp_icon());
			tv_appName.setText(userappInfo.getApp_name());
			tv_app_version.setText("版本：" + userappInfo.getVersionname());
			btn_uninstall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Intent intent = new Intent();
					 Uri uri = Uri.parse("package:"+userappInfo.getApp_PackName());//获取删除包名的URI
		             Intent intent = new Intent(Intent.ACTION_DELETE);
		             intent.setAction(Intent.ACTION_DELETE);
		             intent.setData(uri);
		             Toast.makeText(AppManageActivity.this, userappInfo.getApp_PackName(), 0).show();
		             //   context.startActivity(intent);
					/*intent.setAction("android.intent.action.VIEW");
					intent.setAction("android.intent.action.DELETE");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setData(Uri.parse("package:" + appInfo.getApp_PackName()));*/
					startActivityForResult(intent, 0);
				}
			});

			return convertView;

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			if (observer != null) {
				super.unregisterDataSetObserver(observer);
			}
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 刷新界面。
		SortApp();
		super.onActivityResult(requestCode, resultCode, data);
	}
	private class mysysBaseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return systemAppInfos.size();
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
			ViewHolder holder_sys;
			if (convertView != null) {
				holder_sys = (ViewHolder) convertView.getTag();

			} else {
				convertView = View.inflate(AppManageActivity.this,
						R.layout.sysapp_list_item, null);
				holder_sys = new ViewHolder();
				holder_sys.iv_sys_icon = (ImageView) convertView
						.findViewById(R.id.iv_sys_icon);
				holder_sys.tv_sys_version = (TextView) convertView
						.findViewById(R.id.tv_sys_version);
				holder_sys.tv_sysName = (TextView) convertView
						.findViewById(R.id.tv_sysName);
				convertView.setTag(holder_sys);
			}

			appInfo = systemAppInfos.get(position);
			holder_sys.iv_sys_icon.setImageDrawable(appInfo.getApp_icon());
			holder_sys.tv_sysName.setText(appInfo.getApp_name());
			holder_sys.tv_sys_version.setText("版本：" + appInfo.getVersionname());
			return convertView;

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			if (observer != null) {
				super.unregisterDataSetObserver(observer);
			}
		}

	}

	static class ViewHolder {
		TextView tv_sys_version;
		TextView tv_sysName;
		ImageView iv_sys_icon;
	}
}
