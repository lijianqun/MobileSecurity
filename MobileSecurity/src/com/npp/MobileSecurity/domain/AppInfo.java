package com.npp.MobileSecurity.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private Drawable app_icon;
	private String app_name;
	private String app_PackName;
	private boolean InRom;
	private boolean userApp;
	private long App_size;
	private String versionname;
	private long lastupdate_time;

	public Drawable getApp_icon() {
		return app_icon;
	}

	public void setApp_icon(Drawable app_icon) {
		this.app_icon = app_icon;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getApp_PackName() {
		return app_PackName;
	}

	public void setApp_PackName(String app_PackName) {
		this.app_PackName = app_PackName;
	}

	public boolean isUserApp() {
		return InRom;
	}

	public void setUserApp(boolean isUserApp) {
		this.InRom = isUserApp;
	}

	public boolean isInRom() {
		return InRom;
	}

	public void setInRom(boolean inRom) {
		InRom = inRom;
	}

	public long getApp_size() {
		return App_size;
	}

	public void setApp_size(long app_size) {
		App_size = app_size;
	}

	public String getVersionname() {
		return versionname;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

	public long getLastupdate_time() {
		return lastupdate_time;
	}

	public void setLastupdate_time(long lastupdate_time) {
		this.lastupdate_time = lastupdate_time;
	}

}
