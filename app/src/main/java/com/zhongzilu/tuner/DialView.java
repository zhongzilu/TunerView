package com.zhongzilu.tuner;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by yueba on 2016/5/25 0025.
 */
public class DialView extends View {

    private int mWidth;
    private int mHeight;
    private float mAngle = 0;   //指针旋转角度，值为0时指针垂直显示
    private static int mMaxValue = 50;  //分值区间的最大值
    private static int mMinValue = -50; //分值区间的最小值
    /**输入值，通过输入值来计算指针的旋转角度，即{@mAngle}的值,
     * 最终在界面上呈现的效果是指针指向输入值的刻度上*/
    private float mValue = 30;
    /**刻度盘呈现的总弧度，本案例中总弧度为180，呈半圆形*/
    private float mArc = 180;
//    private ValueAnimator animator;

    public DialView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取屏幕的宽高
        WindowManager windowManager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        mWidth = windowManager.getDefaultDisplay().getWidth();
        mHeight = windowManager.getDefaultDisplay().getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int xc = mWidth / 2;    //圆中心X坐标
        int yc = mHeight / 2;   //圆中心Y坐标
        int radius = xc - 30;   //圆半径

        //画刻度盘
        Paint paintDegree = new Paint();
        paintDegree.setStrokeWidth(3);

        /**
         * 正常情况下，一开始画线或写文字，都是垂直的，但我们的仪表盘
         * 要求从左边开始画，文字是水平的，所以需要先进行画布旋转90°
         */
        canvas.rotate((-mArc / 2), xc, yc);
        System.out.println("before rotate==>" + (-mArc / 2));
        /**
         * 本案例中，设置的分值区间为（-50 ~ 50)
         */
        for (int i = mMinValue; i <= mMaxValue; i++){
            //区别整点和非整点
            if (i % 10 == 0){
                paintDegree.setStrokeWidth(5);
                paintDegree.setTextSize(30);
                paintDegree.setAntiAlias(true);
                paintDegree.setColor(getResources().getColor(R.color.colorAccent));
                canvas.drawLine(xc, yc - radius, xc, yc - radius + 40, paintDegree);
                String degree = String.valueOf(i);
                canvas.drawText(degree,
                        //这里使用了Paint对象的measureText()方法,
                        //该方法是传入一个String类型的参数，经过计算之后返回该String对象中
                        //文字所占用的宽度
                        xc - paintDegree.measureText(degree) / 2,
                        yc - radius + 70,
                        paintDegree);
                /**
                 * 由于两个整点之间只被分为了5分，所以每份的间隔就是2
                 */
            } else if (i % 2 == 0){
                paintDegree.setStrokeWidth(3);
                paintDegree.setTextSize(15);
                paintDegree.setAntiAlias(true);
                paintDegree.setColor(getResources().getColor(R.color.grey));
                canvas.drawLine(xc, yc - radius + 10, xc, yc - radius + 30, paintDegree);

            }

            //通过旋转画布简化坐标运算
            canvas.rotate(mArc / (float)(mMaxValue - mMinValue), xc, yc);
        }

        //画圆盘
        Paint bigCircle = new Paint();
        bigCircle.setColor(getResources().getColor(R.color.colorPrimary));
        bigCircle.setAntiAlias(true);
        canvas.drawCircle(xc, yc, xc - 180, bigCircle);
        //画中等圆盘
        Paint midCircle = new Paint();
        midCircle.setColor(getResources().getColor(R.color.colorPrimaryDark));
        midCircle.setAntiAlias(true);
        canvas.drawCircle(xc, yc, xc - 280, midCircle);
        //画小圆盘
        Paint smallCircle = new Paint();
        smallCircle.setColor(getResources().getColor(R.color.colorAccent));
        smallCircle.setAntiAlias(true);
        canvas.drawCircle(xc, yc, xc - 320, smallCircle);
        canvas.save();

        //画指针
        Paint paintCursor = new Paint();
        paintCursor.setStrokeWidth(3);
        paintCursor.setAntiAlias(true);
        paintCursor.setColor(getResources().getColor(R.color.colorAccent));
        /**
         * 圆方程：(x - a)^2 + (y - b)^2 = r^2
         * (a, b)圆心
         * x:圆上点的x坐标
         * y:圆上点的y坐标
         * r:半径
         *
         * 思路：要想让指针以圆心为中心旋转一定角度，要么旋转画布，要么根据坐标来画，
         * 由于旋转角度比根据坐标更简单，所以就用旋转角度的方式来实现
         *
         * 指针具体旋转多少度，得根据算法来计算
         *
         * 算法：角度 = 输入值 * （ 总弧度 / 分值总数）
         *
         * 举例：本案例中给出的分值区间为（-50 ~ 50）,所以分值总数为100
         * 假设现在输入值为30，那么角度就为54°
         */
        mAngle = mValue * (mArc / (float) (mMaxValue - mMinValue));
        System.out.println("mAngle ==>" + mAngle);
        canvas.rotate(mAngle, xc, yc);
        canvas.drawLine(xc, yc, xc - radius + 80, yc + 10, paintCursor);

        //覆盖在指针上的圆


        /**
         * 由于之前的坐标系已经发生了旋转，所以要在正下方写上文字，就需要旋转回来
         * 当然也可以通过去计算坐标来显示在正下方，但旋转画布的方式更加简单和更容易理解
         */
        canvas.rotate(-(mAngle + mArc / 2), xc, yc);

        Paint pitchPaint = new Paint();
        pitchPaint.setAntiAlias(true);
        pitchPaint.setTextSize(40);
        pitchPaint.setColor(getResources().getColor(R.color.colorAccent));
        String pitch = "Pitch";
        canvas.drawText(pitch,
                xc - pitchPaint.measureText(pitch) / 2,
                //这里加上180，是因为要加上最外面那个大的圆形的半径
                //再加上80，是为了让文字和最大的那个圆产生间隙
                yc + 180 + 80,
                pitchPaint);

        //值
        Paint pitchValuePaint = new Paint();
        pitchValuePaint.setAntiAlias(true);
        pitchValuePaint.setTextSize(50);
        pitchValuePaint.setColor(getResources().getColor(R.color.colorAccent));
        String value = "- -";
        canvas.drawText(value,
                xc - pitchValuePaint.measureText(value) / 2,
                yc + 180 + 130,
                pitchValuePaint);

        canvas.restore();
    }

//    public void setValue(float value){
//        mAngle = value * ( mArc / (float)(mMaxValue - mMinValue));
//        animator = ValueAnimator.ofFloat(0f, mAngle);
//        animator.start();
//
//        invalidate();
//    }

    public void setMaxValue(int maxValue){
        this.mMaxValue = maxValue;
    }

    public void setMinValue(int minValue){
        this.mMinValue = minValue;
    }
}
