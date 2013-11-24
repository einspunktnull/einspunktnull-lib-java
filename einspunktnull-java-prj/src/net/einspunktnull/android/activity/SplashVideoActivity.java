package net.einspunktnull.android.activity;

import android.content.Intent;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.VideoView;

public abstract class SplashVideoActivity extends EinspunktnullActivity
{

	protected int rawVideoHdId;
	protected int rawVideoSdHqId;
	protected int rawVideoSdLqId;
	protected Class<?> mainActivityClass;

	protected abstract int setRawVideoHdId();

	protected abstract int setRawVideoSdHqId();

	protected abstract int setRawVideoSdLqId();

	protected abstract Class<?> setMainActivityClass();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		rawVideoHdId = setRawVideoHdId();
		rawVideoSdHqId = setRawVideoSdHqId();
		rawVideoSdLqId = setRawVideoSdLqId();
		mainActivityClass = setMainActivityClass();
		splashPlayer();

	}

	private void splashPlayer()
	{

		final VideoView videoView = new VideoView(this);
		setContentView(videoView);
		int videoId;

		boolean hd1 = CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P);
		boolean hd2 = CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P);
		boolean sdHq = CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P);
		boolean sdLq = CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_QCIF);

		if (hd1 || hd2)
		{
			videoId = rawVideoHdId;
		}
		else if (sdHq)
		{
			videoId = rawVideoSdHqId;
		}
		else if (sdLq)
		{
			videoId = rawVideoSdLqId;
		}
		else
		{
			jumpMain();
			return;
		}

		Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoId);
		videoView.setVideoURI(videoUri);
		videoView.setOnCompletionListener(new OnCompletionListener()
		{

			public void onCompletion(MediaPlayer mp)
			{
				jumpMain(); // jump to the next Activity
			}
		});

		videoView.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				videoView.stopPlayback();
				jumpMain();
				return true;
			}

		});

		videoView.start();
	}

	protected synchronized void jumpMain()
	{
		Intent intent = new Intent(this, mainActivityClass);
		startActivity(intent);
		finish();
	}
	
//	@Override
//	protected void onPause()
//	{
//		super.onPause();
//		finish();
//	}

}
