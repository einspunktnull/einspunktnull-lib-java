package net.einspunktnull.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapUtil
{

	public static Bitmap getBitmapResource(Context ctx, int resId, Options options)
	{
		if (options != null) return BitmapFactory.decodeResource(ctx.getResources(), resId, options);
		return BitmapFactory.decodeResource(ctx.getResources(), resId);
	}

	public static Bitmap getBitmapResource(Context ctx, int resId)
	{
		return getBitmapResource(ctx, resId, null);
	}
}
