package com.npp.MobileSecurity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		//判断是否做过设置向导。
		sp=getSharedPreferences("config", MODE_PRIVATE);
		boolean configed =sp.getBoolean("configed", false);
		if(configed)
		{
			//设置过想到进入到这个主页面
			setContentView(R.layout.activity_lostfind);
			
		}
		else
		{
			intent=new Intent(this, SetupOneActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
