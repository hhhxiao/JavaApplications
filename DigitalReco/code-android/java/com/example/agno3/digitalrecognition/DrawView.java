package com.example.agno3.digitalrecognition;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

import java.util.ArrayList;

public class DrawView extends View {
    private Bitmap cacheBitmap;// 画纸
    private Canvas cacheCanvas;// 创建画布、画家
    private Path path;// 绘图的路径
    private Paint paint;// 画笔
    private float preX, preY;// 之前的XY的位置，用于下面的手势移动


    //初始化
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        paint = new Paint();
        cacheCanvas = new Canvas();
        // 获取屏幕的高度与宽度
        int view_width = context.getResources().getDisplayMetrics().widthPixels;
        // 屏幕的高度与宽度
        cacheBitmap = Bitmap.createBitmap(view_width,view_width,
                Config.ARGB_8888);// 建立图像缓冲区用来保存图像
        cacheCanvas.setBitmap(cacheBitmap);
        cacheCanvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLACK);// 设置画笔的默认颜色
        paint.setStyle(Paint.Style.STROKE);// 设置画笔的填充方式为无填充、仅仅是画线
        paint.setStrokeWidth(50);// 设置画笔的宽度为1

    }

    //方法重写
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(cacheBitmap, 0, 0, paint);// 把cacheBitmap画到DrawView上
    }
    //画画
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 获取触摸位置
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {// 获取触摸的各个瞬间
            case MotionEvent.ACTION_DOWN:// 手势按下
                path.moveTo(x, y);// 绘图的起始点
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if (dx > 5 || dy > 5) {// 用户要移动超过5像素才算是画图，免得手滑、手抖现象
                    path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                    preX = x;
                    preY = y;
                    cacheCanvas.drawPath(path, paint);// 绘制路径
                }
                break;
            case MotionEvent.ACTION_UP:
                path.reset();
                break;
        }
        invalidate();
        return true;
    }


    //识别
    void reco(Activity activity, ArrayList<double[][]> filters, double[][] W_1, double[][] W_2) throws Exception {
        if(filters == null || W_2 == null ||W_1 ==null){
            Log.d("Reco","数据读入失败");
        }else
        {
            double[][] image = getGrayMatrix();//获取灰度矩阵

            ArrayList<double[][]> cov_maps = new ArrayList<>();//卷积图组
            for (double[][] filter:filters) {
                double[][] cov_map = MathUtil.conv(image,filter);
                cov_maps.add(cov_map);
            }


            double[][] pool_array = MathUtil.pool(cov_maps);//池化数组


            DoubleMatrix2D pool_matrix = new DenseDoubleMatrix2D(pool_array);
            DoubleMatrix2D hide_W = new DenseDoubleMatrix2D(W_1);


            double[][]  ma =  Algebra.DEFAULT.mult(hide_W,pool_matrix).toArray();
            for (int i = 0; i < ma.length; i++) {
                for (int j = 0; j < ma[0].length; j++) {
                   if(ma[i][j]<0)ma[i][j] = 0;
                }
            }

            DoubleMatrix2D matrix2D = new DenseDoubleMatrix2D(ma);

            DoubleMatrix2D output_W = new DenseDoubleMatrix2D(W_2);
            double[][]  output =  Algebra.DEFAULT.mult(output_W,matrix2D).toArray();
            double max = -100000;
            int ind = 0;
            for (int i = 0; i < output.length; i++) {
                Log.d(""+i,output[i][0]+"");
                if(max<output[i][0])
                {   ind = i+1;
                    max = output[i][0];
                }
            }
           if(ind == 10)ind = 0;
           Toast.makeText(activity,"answer is"+ind,Toast.LENGTH_SHORT).show();

        }
    }


    //获得画布上图案的灰度矩阵
    private double[][] getGrayMatrix() throws Exception {

        double[][] grayMatrix = new double[28][28];
        Bitmap bitmap = resizeImage(cacheBitmap, 28);
        int color;
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                assert bitmap != null;
                color = bitmap.getPixel(i, j);
                if (color == 0) {
                    grayMatrix[j][i] = 0;
                } else {
                    grayMatrix[j][i] = 1;
                }
            }
        }
        return grayMatrix;

    }


    //清空画布
    public void clear() {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        cacheCanvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

    }


    //把画布上的图案压缩到28*28
    private Bitmap resizeImage(Bitmap bitmap, int newSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth;
        int newHeight;

        if (width > height) {
            newWidth = newSize;
            newHeight = (newSize * height) / width;
        } else if (width < height) {
            newHeight = newSize;
            newWidth = (newSize * width) / height;
        } else {
            newHeight = newSize;
            newWidth = newSize;
        }

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0,
                width, height, matrix, true);
    }

}
