package com.npp.MobileSecurity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.npp.MobileSecurity.ui.SettingItemView;

public class SettingActivity extends Activity {
	private SettingItemView siv_update;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean update= sp.getBoolean("update", false);
		if(update){
			siv_update.setChecked(true);
			siv_update.setDesc("自动更新已打开");
			
		}
		else{
			siv_update.setChecked(false);
			siv_update.setDesc("自动更新已关闭");
		}

		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				// TODO Auto-generated method stub
				if (siv_update.isChecked()) {// 已经打开自动升级
					siv_update.setChecked(false);
					siv_update.setDesc("自动更新已关闭");
					editor.putBoolean("update", false);
				} else {// 没有打开自动升级
					siv_update.setChecked(true);
					siv_update.setDesc("自动更新已打开");
					editor.putBoolean("update", true);
				}
				editor.commit();

			}
		});
	}

}
