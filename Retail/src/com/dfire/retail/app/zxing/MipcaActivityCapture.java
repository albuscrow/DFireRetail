package com.dfire.retail.app.zxing;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daoshun.lib.util.DensityUtils;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.zxing.camera.CameraManager;
import com.dfire.retail.app.zxing.decoding.CaptureActivityHandler;
import com.dfire.retail.app.zxing.decoding.InactivityTimer;
import com.dfire.retail.app.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private ImageButton mBack;
	private ImageView mImg;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		// ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		mImg = (ImageView) findViewById(R.id.ac_ca_white_juhua);

		// Button mButtonBack = (Button) findViewById(R.id.button_back);
		// mButtonBack.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// MipcaActivityCapture.this.finish();
		//
		// }
		// });
		mBack = (ImageButton) findViewById(R.id.title_back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MipcaActivityCapture.this.finish();
			}
		});
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			try {
				initCamera(surfaceHolder);
			} catch (Exception e) {
				Toast.makeText(MipcaActivityCapture.this, "摄像头权限已被禁止", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 回调函数
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			System.out.println(resultString + "=\"\"");
			viewfinderView.setSuccess(false);
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		} else {
			viewfinderView.setSuccess(true);

			mImg.setVisibility(View.VISIBLE);
			// Bitmap circlebmp = BitmapFactory.decodeResource(getResources(), R.drawable.white_juhua); //加载图片a1
			// ImageView img = new ImageView(this);
			// img.setBackgroundResource(R.drawable.white_juhua);
			int frameLeft = viewfinderView.getPaddingLeft();
			int frameRight = viewfinderView.getPaddingRight();
			int frameBottom = viewfinderView.getPaddingBottom();
			int left = (frameLeft + frameRight) / 2 - DensityUtils.dp2px(MipcaActivityCapture.this, 44) / 2;
			// int right = (frameLeft + frameRight)/2 + mImg.getWidth() / 2;
			// int point = DensityUtils.dp2px(MipcaActivityCapture.this, 100);
			int bottom = frameBottom - DensityUtils.dp2px(MipcaActivityCapture.this, 100);
			int top = bottom - mImg.getHeight();
			System.out.println(left + "___" + left);
			((RelativeLayout.LayoutParams) mImg.getLayoutParams()).leftMargin = left;
			((RelativeLayout.LayoutParams) mImg.getLayoutParams()).topMargin = top;
			// ((RelativeLayout.LayoutParams)mImg.getLayoutParams()).rightMargin = right;
			// ((RelativeLayout.LayoutParams)mImg.getLayoutParams()).bottomMargin = bottom;
			// WindowManager h = MipcaActivityCapture.this.getWindowManager();
			// h.addView(img, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			RotateAnimation animation = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setInterpolator(new LinearInterpolator());// 匀速旋转
			animation.setRepeatCount(-1);
			animation.setDuration(800);
			mImg.startAnimation(animation);

			System.out.println(resultString + "!=\"\"");
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			// 貌似传的图片太大的原因，这边不需要，先去掉
			// bundle.putParcelable("bitmap", barcode);
			resultIntent.putExtras(bundle);
			resultIntent.putExtra(Constants.DEVICE_CODE, resultString);
			mediaPlayer = MediaPlayer.create(this, R.raw.beep);
			
			this.setResult(RESULT_OK, resultIntent);
		}
		MipcaActivityCapture.this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) throws Exception {
		CameraManager.get().openDriver(surfaceHolder);

		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			try {
				initCamera(holder);
			} catch (Exception e) {
				Toast.makeText(MipcaActivityCapture.this, "摄像头权限已被禁止", Toast.LENGTH_SHORT).show();
				return;
			}
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
//			mediaPlayer = MediaPlayer.create(this, R.raw.beep);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				e.printStackTrace();
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}