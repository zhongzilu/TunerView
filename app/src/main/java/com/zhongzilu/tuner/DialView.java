package com.zhongzilu.tuner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by zhongzilu on 2016/5/25 0025.
 */
public class DialView extends View {

    private float mWidth;
    private float mHeight;
    private float mAngle = 0;   //指针旋转角度，值为0时指针垂直显示
    private static int mMaxValue = 50;  //分值区间的最大值
    private static int mMinValue = -50; //分值区间的最小值
    /**输入值，通过输入值来计算指针的旋转角度，即{@mAngle}的值,
     * 最终在界面上呈现的效果是指针指向输入值的刻度上*/
    private float mValue = 0;
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

    /**
     * 注意：
     * 下方的所以坐标计算和长度计算都是依据在屏幕宽度为621px情况下的
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float xc = mWidth / 2;    //圆中心X坐标
        float yc = mHeight / 2;   //圆中心Y坐标
        float radius = (float)(xc - xc * 0.05314);   //圆半径

        //画刻度盘
        Paint paintDegree = new Paint();

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
                //在屏幕宽度为621下，大小为5
                paintDegree.setStrokeWidth((float)(mWidth * 0.008051));
                //刻度字体大小是依据是：在屏幕宽度为621的分辨率下，刚好为30
                paintDegree.setTextSize((float)(mWidth * 0.048309));
                paintDegree.setAntiAlias(true);
                paintDegree.setColor(getResources().getColor(R.color.colorAccent));
                canvas.drawLine(xc, yc - radius, xc, (float)(yc - radius + xc * 0.101449), paintDegree);
                String degree = String.valueOf(i);
                canvas.drawText(degree,
                        //这里使用了Paint对象的measureText()方法,
                        //该方法是传入一个String类型的参数，经过计算之后返回该String对象中
                        //文字所占用的宽度
                        xc - paintDegree.measureText(degree) / 2,

                        //下方mWidth * 0.048309 = 在屏幕宽度为621下，长度为30
                        yc - radius + (float)(xc * 0.101449 + mWidth * 0.048309),
                        paintDegree);
                /**
                 * 由于两个整点之间只被分为了5分，所以每份的间隔就是2
                 */
            } else if (i % 2 == 0){
                paintDegree.setStrokeWidth((float)(mWidth * 0.004830));
                //在屏幕宽度为621下，字体大小为15
                paintDegree.setTextSize((float)(mWidth * 0.024154));
                paintDegree.setAntiAlias(true);
                paintDegree.setColor(getResources().getColor(R.color.grey));
                /**
                 * 画灰色短横线，坐标计算依据是灰色短横向长度为深色长横线的一半，
                 * 并且，两种刻度线的中点在同一个圆的圆弧上
                 * 由于深色刻度线的长度 = 正中深色小圆的半径 = 屏幕宽度一半的0.101449倍
                 */
                canvas.drawLine(xc,
                        (float)(yc - radius + xc * 0.101449 / 4),
                        xc,
                        (float)(yc - radius + xc * 0.101449 / 4 * 3),
                        paintDegree);

            }

            //通过旋转画布简化坐标运算
            canvas.rotate(mArc / (float)(mMaxValue - mMinValue), xc, yc);
        }

        /**
         * 三个圆盘的半径和屏幕宽度的一半的比例为
         * 屏幕宽度一半 ：大圆 ：中圆 ：小圆 =
         * 1 ：0.481481 ：0.201288 ：0.101449
         */
        //画大圆盘
        Paint bigCircle = new Paint();
        bigCircle.setColor(getResources().getColor(R.color.colorPrimary));
        bigCircle.setAntiAlias(true);
        canvas.drawCircle(xc, yc, (float)(xc * 0.481481), bigCircle);
        //画中等圆盘
        Paint midCircle = new Paint();
        midCircle.setColor(getResources().getColor(R.color.colorPrimaryDark));
        midCircle.setAntiAlias(true);
        canvas.drawCircle(xc, yc, (float)(xc * 0.201288), midCircle);
        //画小圆盘
        Paint smallCircle = new Paint();
        smallCircle.setColor(getResources().getColor(R.color.colorAccent));
        smallCircle.setAntiAlias(true);
        canvas.drawCircle(xc, yc, (float)(xc * 0.101449), smallCircle);
        canvas.save();

        //画指针
        Paint paintCursor = new Paint();
        //在屏幕宽度为621下，大小为3
        paintCursor.setStrokeWidth((float)(mWidth * 0.004830));
        paintCursor.setAntiAlias(true);
        paintCursor.setColor(getResources().getColor(R.color.colorAccent));
        /**
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
        /**
         * xc * 0.101449 = 深色刻度线条的长度
         * mWidth * 0.064412 = 在屏幕宽度为621下，长度为40
         * 所以下面第三个参数的值可以理解为距离深色刻度线下方有40倍数的间隙
         */
        canvas.drawLine(xc, yc,
                xc - radius + (float)(xc * 0.101449 + mWidth * 0.064412),
                yc + 3, paintCursor);

        //覆盖在指针上的圆
        /**
         * 由于之前的坐标系已经发生了旋转，所以要在正下方写上文字，就需要旋转回来
         * 当然也可以通过去计算坐标来显示在正下方，但旋转画布的方式更加简单和更容易理解
         */
        canvas.rotate(-(mAngle + mArc / 2), xc, yc);

        Paint pitchPaint = new Paint();
        pitchPaint.setAntiAlias(true);
        //在屏幕宽度为621下，字体大小为40
        pitchPaint.setTextSize((float)(mWidth * 0.048309));
        pitchPaint.setColor(getResources().getColor(R.color.colorAccent));
        String pitch = "Pitch";
        canvas.drawText(pitch,
                xc - pitchPaint.measureText(pitch) / 2,
                //这里加上180，是因为要加上最外面那个大的圆形的半径
                //再加上(mWidth * 0.123188)，是为了让文字和最大的那个圆产生间隙
                //在屏幕宽度为621下，间隙为80
                yc + (float)(xc * 0.481481 + mWidth * 0.123188),
                pitchPaint);

        //值
        Paint pitchValuePaint = new Paint();
        pitchValuePaint.setAntiAlias(true);
        pitchValuePaint.setTextSize((float)(mWidth * 0.080515));
        pitchValuePaint.setColor(getResources().getColor(R.color.colorAccent));
        String value = "- -";
        canvas.drawText(value,
                xc - pitchValuePaint.measureText(value) / 2,
                yc + (float)(xc * 0.481481 + mWidth * 0.209339),
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
