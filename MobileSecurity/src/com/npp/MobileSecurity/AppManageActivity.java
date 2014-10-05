package com.npp.MobileSecurity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.widget.TextView;

public class AppManageActivity extends Activity {
	private TextView tv_ROMsize;
	private TextView tv_SDsize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanage);
		tv_ROMsize = (TextView) findViewById(R.id.tv_ROMsize);
		tv_SDsize = (TextView) findViewById(R.id.tv_Sdsize);
		// 获得可用内存
		long RomSize = getPathAvlibeSpace(Environment.getDataDirectory()
				.getAbsolutePath());
		// 获得可用外存
		long SDSize = getPathAvlibeSpace(Environment
				.getExternalStorageDirectory().getAbsolutePath());
		tv_ROMsize.setText("剩余ROM:" + Formatter.formatFileSize(this, RomSize));
		tv_SDsize.setText("剩余SD卡:" + Formatter.formatFileSize(this, SDSize));
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

}
