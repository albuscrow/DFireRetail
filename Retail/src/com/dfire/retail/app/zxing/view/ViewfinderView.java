/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dfire.retail.app.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.daoshun.lib.util.DensityUtils;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.zxing.camera.CameraManager;
import com.google.zxing.ResultPoint;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 */
public final class ViewfinderView extends View {
	private static final String TAG = "log";
	/**
	 * ˢ�½����ʱ��
	 */
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;

	/**
	 * �ĸ���ɫ�߽Ƕ�Ӧ�ĳ���
	 */
	private int ScreenRate;
	
	/**
	 * ɨ����е��м��ߵĿ��
	 */
	private static final int MIDDLE_LINE_WIDTH = 6;
	
	/**
	 * ɨ����е��м��ߵ���ɨ������ҵļ�϶
	 */
	private static final int MIDDLE_LINE_PADDING = 5;
	
	/**
	 * �м�������ÿ��ˢ���ƶ��ľ���
	 */
	private static final int SPEEN_DISTANCE = 5;
	
	/**
	 * �ֻ�����Ļ�ܶ�
	 */
	private static float density;
	/**
	 * �����С
	 */
	private static final int TEXT_SIZE = 16;
	/**
	 * �������ɨ�������ľ���
	 */
	private static final int TEXT_PADDING_TOP = 30;
	
	/**
	 * ���ʶ��������
	 */
	private Paint paint;
	
	/**
	 * �м们���ߵ����λ��
	 */
	private int slideTop;
	
	/**
	 * �м们���ߵ���׶�λ��
	 */
	private int slideBottom;
	
	/**
	 * ��ɨ��Ķ�ά��������������û��������ܣ���ʱ������
	 */
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	
	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	boolean isFirst;
	private Context context;
	private boolean isSuccess = false;
	
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		density = context.getResources().getDisplayMetrics().density;
		//������ת����dp
		ScreenRate = (int)(20 * density);

		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);

		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@Override
	public void onDraw(Canvas canvas) {
		//�м��ɨ�����Ҫ�޸�ɨ���Ĵ�С��ȥCameraManager�����޸�
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		
		//��ʼ���м��߻��������ϱߺ����±�
		if(!isFirst){
			isFirst = true;
			slideTop = frame.top;
			slideBottom = frame.bottom;
		}
		
		//屏幕宽高
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		
		//半透明背景边框
		canvas.drawRect(0, 0, width, frame.top, paint);//上面
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);//左边，与camera等高
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);//右边，与camera等高
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);//下面
		
		

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			//画边框线
			paint.setColor(Color.parseColor("#e0e2e4"));
			final int LINE_WIDTH = DensityUtils.dp2px(context, 20);//边框线长度
			final int CORNER_WIDTH = DensityUtils.dp2px(context, 2);//边框线厚度
			canvas.drawRect(frame.left, frame.top, frame.left + LINE_WIDTH,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
					+ LINE_WIDTH, paint);
			canvas.drawRect(frame.right - LINE_WIDTH, frame.top, frame.right,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
					+ LINE_WIDTH, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
					+ LINE_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - LINE_WIDTH,
					frame.left + CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - LINE_WIDTH, frame.bottom - CORNER_WIDTH,
					frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - LINE_WIDTH,
					frame.right, frame.bottom, paint);

			
			//扫描线
			slideTop += SPEEN_DISTANCE;
			if(slideTop >= frame.bottom){
				slideTop = frame.top;
			}
			if(isSuccess){
				
			}else{
				Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.scan_line); //加载图片a1
				/**
				 * drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint)；
				 * Rect src: 是对图片进行裁截，若是空null则显示整个图片
				 * RectF dst：是图片在Canvas画布中显示的区域，
				 * 大于src则把src的裁截区放大，
				 * 小于src则把src的裁截区缩小。
				 */
				Rect rect = new Rect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH/2, frame.right - MIDDLE_LINE_PADDING,slideTop + MIDDLE_LINE_WIDTH/2);
				canvas.drawBitmap(bmp,  null, rect, paint);
			}
			
			
			//画扫描的字
			paint.setColor(Color.parseColor("#ffffff"));
			
			
			
//			paint.setAlpha(0x40);
			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
			paint.setTextAlign(Align.CENTER);//锚点居中
			String drawText = "";
			int textSize;
			int marginTop;
			int marginBottom;
			if(isSuccess){
				
				drawText = "扫码成功!";
				textSize = DensityUtils.dp2px(context, 24);
				paint.setTextSize(textSize);
				//锚点位于中间的底部，需要在加上字的高度
				marginTop = DensityUtils.dp2px(context, 18 + 24);
				canvas.drawText(drawText, (frame.left + frame.right) / 2, (float) (frame.top + (float)marginTop), paint);
				
				drawText = "请稍候...";
				marginBottom = DensityUtils.dp2px(context, 27);
				textSize = DensityUtils.dp2px(context, 14);
				paint.setTextSize(textSize);
				canvas.drawText(drawText, (frame.left + frame.right) / 2, (float) (frame.bottom - (float)marginBottom), paint);
				
				
//				marginBottom = DensityUtils.dp2px(context, 100);
//				Bitmap circlebmp = BitmapFactory.decodeResource(getResources(), R.drawable.white_juhua); //加载图片a1
//				canvas.drawBitmap(circlebmp, (frame.left + frame.right) / 2 - circlebmp.getWidth() / 2,
//						(float) (frame.bottom - (float)marginBottom), paint);
			}else{
				drawText = "将二维码/条码放入框内, 即可自动扫描";
				textSize = DensityUtils.dp2px(context, 13);
				paint.setTextSize(textSize);
				marginTop = DensityUtils.dp2px(context, 30);
				canvas.drawText(drawText, (frame.left + frame.right) / 2, (float) (frame.bottom + (float)marginTop), paint);
			}

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 3.0f, paint);
				}
			}

			
			//画布刷新
			if(isSuccess){
				invalidate();
			}else{
				postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
						frame.right, frame.bottom);
			}
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}
	public void setSuccess(boolean isSuccess){
		this.isSuccess = isSuccess;
	}
	public int getPaddingLeft(){
		return CameraManager.get().getFramingRect().left;
	}
	public int getPaddingRight(){
		return CameraManager.get().getFramingRect().right;
	}
	public int getPaddingBottom(){
		return CameraManager.get().getFramingRect().bottom;
	}

}
