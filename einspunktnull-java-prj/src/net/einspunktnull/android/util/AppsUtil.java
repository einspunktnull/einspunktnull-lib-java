package net.einspunktnull.android.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class AppsUtil
{

	/*******************************************************
	 * SERVICE
	 *******************************************************/
	public static boolean isServiceRunning(Context context, String serviceName)
	{
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
		{
			if (serviceName.equals(service.service.getClassName())) { return true; }
		}
		return false;
	}

	public static boolean isServiceRunning(Context context, Class<?> serviceClass)
	{
		String serviceName = serviceClass.getName();
		return isServiceRunning(context, serviceName);
	}

	/*******************************************************
	 * APP
	 *******************************************************/

	public static AppInfo getAppInfo(Context ctx, String pkgName)
	{
		PackageManager pkgMgr = ctx.getPackageManager();
		try
		{
			ApplicationInfo applicationInfo = pkgMgr.getApplicationInfo(pkgName, 0);
			PackageInfo pkgInfo = pkgMgr.getPackageInfo(pkgName, 0);
			AppInfo appInfo = new AppInfo();
			appInfo.appName = (String) pkgMgr.getApplicationLabel(applicationInfo);
			appInfo.pkgName = pkgName;
			appInfo.icon = pkgMgr.getApplicationIcon(pkgName);
			appInfo.iconId = applicationInfo.icon;
			appInfo.versionName = pkgInfo.versionName;
			appInfo.versionCode = pkgInfo.versionCode;
			return appInfo;
		}
		catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static String getCurrentPackageName(Context context)
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

		RunningTaskInfo runTaskInfo = taskInfo.get(0);
		ComponentName compName = runTaskInfo.topActivity;
		String currPkgName = compName.getPackageName();

		return currPkgName;
	}

	public static List<ApplicationInfo> getInstalledApps(Context ctx)
	{
		PackageManager pm = ctx.getPackageManager();

		return pm.getInstalledApplications(PackageManager.GET_META_DATA);

	}

	public static ArrayList<AppInfo> getInstalledApps(Context ctx, boolean getSysPackages)
	{
		ArrayList<AppInfo> res = new ArrayList<AppInfo>();
		List<PackageInfo> packs = ctx.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++)
		{
			PackageInfo pkgInfo = packs.get(i);
			if ((!getSysPackages) && (pkgInfo.versionName == null))
			{
				continue;
			}
			AppInfo appInfo = new AppInfo();
			appInfo.appName = pkgInfo.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
			appInfo.pkgName = pkgInfo.packageName;
			appInfo.versionName = pkgInfo.versionName;
			appInfo.versionCode = pkgInfo.versionCode;
			appInfo.icon = pkgInfo.applicationInfo.loadIcon(ctx.getPackageManager());
			appInfo.iconId = pkgInfo.applicationInfo.icon;
			res.add(appInfo);
		}
		return res;
	}

	public static boolean isSystemPackage(PackageInfo pkgInfo)
	{
		return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
	}

	public static boolean isSystemPackage(ResolveInfo ri)
	{
		return ((ri.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
	}

	public static Drawable getApplicationIcon(PackageManager pkgMgr, String pkgName) throws NameNotFoundException
	{
		return pkgMgr.getApplicationIcon(pkgName);
	}

	@SuppressWarnings("serial")
	public static class AppInfo implements Serializable
	{

		public int iconId;
		public transient Drawable icon;
		public int versionCode;
		public String versionName;
		public String pkgName;
		public String appName;

	}

}
