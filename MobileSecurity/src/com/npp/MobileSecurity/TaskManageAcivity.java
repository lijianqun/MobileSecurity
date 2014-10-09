package com.npp.MobileSecurity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.npp.MobileSecurity.adapter.ViewPagerAdapter;
import com.npp.MobileSecurity.domain.AppInfo;
import com.npp.MobileSecurity.engine.AppInfoProvider;

public class TaskManageAcivity extends Activity {
	private TextView tv_ROMsize;
	private TextView tv_SDsize;
	private ViewPager viewPager;
	private ListView ll_app_list;
	ViewPagerAdapter viewPagerAdapter;
	private AppInfo appInfo;
	int current;
	private LinearLayout nav_layout;
	private TextView[] nav;

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
	}

}
