package com.sebox.diary.view;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

public class MyWin8Button extends ImageView{
	private Camera camera;   // 声明一个图像的摄像机
	private boolean newOne;  // 声明一个图像是否是第一次加载
	private int realWidth;       // 图片真实的宽度
	private int realHeight;       // 图片真实的高度
	private float x;    // 点击的x坐标
	private float y;    // 点击的y坐标
	private boolean isMiddle;  // 是否是在中间区域位置点击
	private boolean isMove;    // 是否在点击后移出该图像区域
	private final float SCALE_NUM = 0.95f;  // 缩放比例
	private final int ROTATE_NUM = 10;      // 倾斜程度
	private Matrix oldMatrix;   // 未添加任何效果的图像
	private boolean isFinish;   // 是否完成特效
	boolean xBigY = false;      // 判断x是否大于y
	float RolateX = 0;  // x偏移位置
	float RolateY = 0;  // y偏移位置

	public MyWin8Button(Context context) {
		super(context);
		camera = new Camera();   //  定义一个图像摄像机
		newOne = true;           //  定义是第一次加载
		oldMatrix = new Matrix();
		oldMatrix.set(getImageMatrix());  // 获取未添加任何效果的图像
		isFinish = true;  // 设置已完成特效
		isMiddle = false; // 设置不在中间区域
		isMove = false;   // 设置没有移开图像区域
	}
	
	public MyWin8Button(Context context, AttributeSet attributeSet) {
		super(context,attributeSet);
		camera = new Camera();   //  定义一个图像摄像机
		newOne = true;			 //  定义是第一次加载
		oldMatrix = new Matrix();
		oldMatrix.set(getImageMatrix());  // 获取未添加任何效果的图像
		isFinish = true;  // 设置已完成特效
		isMiddle = false; // 设置不在中间区域
		isMove = false;   // 设置没有移开图像区域
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (newOne){   // 如果是一个新的组件 进行初始化 并进行标识
			newOne = false;
			init();    // 进行初始化
		}
		
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
	}
	
	private void init(){
		// 获取真实的图像宽度
		realWidth = getWidth() - getPaddingLeft() - getPaddingRight();
		// 获取真实的图像高度
		realHeight = getHeight() - getPaddingTop() - getPaddingBottom();
		
		BitmapDrawable bd = (BitmapDrawable) getDrawable();  // 获取位图
		bd.setAntiAlias(true);   // 允许使用抗锯齿等功能
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {  // 触摸事件的响应
		super.onTouchEvent(event);
		
		// 判断事件  并且消除非触摸事件
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 响应按下的事件
		case MotionEvent.ACTION_DOWN:
			x = event.getX();  // 获取点击的x坐标
			y = event.getY();  // 获取点击的y坐标
			
			RolateX = realWidth / 2 - x;
			RolateY = realHeight / 2 - y;
			xBigY = Math.abs(RolateX) > Math.abs(RolateY) ? true : false;
			
			isMove = false;    // 设置未移出该区域
			
			// 判断是否在中间位置
			isMiddle = x > realWidth / 3 && x < realWidth * 2 / 3 && y > realHeight / 3 && y < realHeight * 2 / 3;
			if (isMiddle){
				scaleHandler.sendEmptyMessage(1);
			}
			else {
				rolateHandler.sendEmptyMessage(1);
			}
			
			break;
			
		// 响应移动的事件
		case MotionEvent.ACTION_MOVE:
			float x=event.getX();float y=event.getY();
			if(x > realWidth || y > realHeight || x < 0 || y < 0){
				isMove=true;
			}else{
				isMove=false;
			}
			break;
			
		// 响应抬起的事件
		case MotionEvent.ACTION_UP:
			if (isMiddle) {
				scaleHandler.sendEmptyMessage(6);
			} else {
				rolateHandler.sendEmptyMessage(6);
			}
			break;

		default:
			break;
		}
		return true;
	}
	
	private Handler scaleHandler = new Handler(){
		private Matrix matrix = new Matrix();  // 创建新的matrix
		private int count = 0;    // 调用限定
		private float s;  // 缩放保存值
		
		public void handleMessage(Message msg) {
			
			if (isFinish){  // 如果没完成   获取当前的矩阵
				matrix.set(getImageMatrix()); 
			}
			
			switch (msg.what) {  // 判断值
			case 1:
				if (!isFinish) {  // 没完成返回
					return;
				} else {
					isFinish = false;  // 设定未完成
					count = 0;         
					s = (float) Math.sqrt(Math.sqrt(SCALE_NUM));
					matrix.set(oldMatrix);
					BeginScale(matrix, s);
					scaleHandler.sendEmptyMessage(2);
				}
				break;
			case 2:
				BeginScale(matrix, s);
				if (count < 4) {
					scaleHandler.sendEmptyMessage(2);
				} else {
					isFinish = true;
				}
				count++;
				break;
			case 6:
				if (!isFinish) {
					scaleHandler.sendEmptyMessage(6);
				} else {
					isFinish = false;
					count = 0;
					s = (float) Math.sqrt(Math.sqrt(1.0f / SCALE_NUM));
					BeginScale(matrix, s);
					scaleHandler.sendEmptyMessage(2);
				}
				break;
			}
			
		};
	};
	
	//  开始进行缩放
	@SuppressWarnings("unused")
	@SuppressLint("HandlerLeak")
	private synchronized void BeginScale(Matrix matrix, float scale) {
		int scaleX = (int) (realWidth * 0.5f);
		int scaleY = (int) (realHeight * 0.5f);
		matrix.postScale(scale, scale, scaleX, scaleY);
		setImageMatrix(matrix);
	}
	
	
	private Handler rolateHandler = new Handler() {
		private Matrix matrix = new Matrix();
		private float count = 0;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			matrix.set(getImageMatrix());
			switch (msg.what) {
			case 1:
				count = 0;
				BeginRolate(matrix, (xBigY ? count : 0), (xBigY ? 0 : count));
				rolateHandler.sendEmptyMessage(2);
				break;
			case 2:
				BeginRolate(matrix, (xBigY ? count : 0), (xBigY ? 0 : count));
				if (count < ROTATE_NUM) {
					rolateHandler.sendEmptyMessage(2);
				} else {
					isFinish = true;
				}
				count++;
				count++;
				break;
			case 3:
				BeginRolate(matrix, (xBigY ? count : 0), (xBigY ? 0 : count));
				if (count > 0) {
					rolateHandler.sendEmptyMessage(3);
				} else {
					isFinish = true;
				}
				count--;
				count--;
				break;
			case 6:
				count = ROTATE_NUM;
				BeginRolate(matrix, (xBigY ? count : 0), (xBigY ? 0 : count));
				rolateHandler.sendEmptyMessage(3);
				break;
			}
		}
	};

	private synchronized void BeginRolate(Matrix matrix, float rolateX,
			float rolateY) {
		// Bitmap bm = getImageBitmap();
		int scaleX = (int) (realWidth * 0.5f);
		int scaleY = (int) (realHeight * 0.5f);
		camera.save();
		camera.rotateX(RolateY > 0 ? rolateY : -rolateY);
		camera.rotateY(RolateX < 0 ? rolateX : -rolateX);
		camera.getMatrix(matrix);
		camera.restore();
		

		if (RolateX > 0 && rolateX != 0) {
			matrix.preTranslate(-realWidth, -scaleY);
			matrix.postTranslate(realWidth, scaleY);
		} else if (RolateY > 0 && rolateY != 0) {
			matrix.preTranslate(-scaleX, -realHeight);
			matrix.postTranslate(scaleX, realHeight);
		} else if (RolateX < 0 && rolateX != 0) {
			matrix.preTranslate(-0, -scaleY);
			matrix.postTranslate(0, scaleY);
		} else if (RolateY < 0 && rolateY != 0) {
			matrix.preTranslate(-scaleX, -0);
			matrix.postTranslate(scaleX, 0);
		}

		
		setImageMatrix(matrix);
	}

}
