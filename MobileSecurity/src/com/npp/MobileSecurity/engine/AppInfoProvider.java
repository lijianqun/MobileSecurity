package com.npp.MobileSecurity.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.npp.MobileSecurity.domain.AppInfo;

/**
 * 
 * @Description TODO
 * @author NingPingPing
 * @date 2014年10月5日 上午10:46:20
 */
public class AppInfoProvider {
	/**
	 * 获取所有手机应用信息
	 * 
	 * @return
	 */
	public static List<AppInfo> getAppInfos(Context context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> PackageInfos = pm.getInstalledPackages(0);
		List<AppInfo> AppInfos = new ArrayList<AppInfo>();
		for (PackageInfo packinfo : PackageInfos) {
			AppInfo appInfo = new AppInfo();
			String packageName = packinfo.packageName;// 包名
			long last_UpdateTime = packinfo.lastUpdateTime;// 最后更新时间
			String versionname = packinfo.versionName;// 版本名
			Drawable app_icon = packinfo.applicationInfo.loadIcon(pm);// 应用图标
			String app_name = packinfo.applicationInfo.loadLabel(pm).toString();// 应用名称

			// 获取应用程序是否是第三方应用程序
			appInfo.setUserApp(filterApp(packinfo.applicationInfo));
			appInfo.setApp_icon(app_icon);
			appInfo.setApp_name(app_name);
			appInfo.setApp_PackName(packageName);
			appInfo.setLastupdate_time(last_UpdateTime);
			appInfo.setVersionname(versionname);
			AppInfos.add(appInfo);
		}
		return AppInfos;
	}

	/**
	 * 三方应用程序的过滤器
	 * 
	 * @param info
	 * @return true 三方应用 false 系统应用
	 */
	public static boolean filterApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			// 代表的是系统的应用,但是被用户升级了. 用户应用
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			// 代表的用户的应用
			return true;
		}
		return false;
	}

}
