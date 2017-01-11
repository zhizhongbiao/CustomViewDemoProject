package demo.com.customviewdemo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Alv_chi on 2016/12/18.
 */
public class MyClockView extends View {

    private Paint circlePaint;
    private Paint pointerPaint;
    private Paint textPaint;


    public MyClockView(Context context) {
        this(context, null);
    }

    public MyClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyClockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//     初始化画笔工具：
        initialPaints();

    }

    private void initialPaints() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.CYAN);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(10);
        pointerPaint = new Paint();
        pointerPaint.setColor(Color.GREEN);
        textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(30);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//     计算圆心坐标，和半径；
        int height = getHeight();
        int width = getWidth();
        int centerY = height / 2;
        int centerX = width / 2;
        int radius = (height > width ? width : height) / 4;

//        画个圆，和圆心；
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
        canvas.drawPoint(centerX, centerY, circlePaint);

//        画钟表上的数字：
        double singleAngle = Math.PI * 2 / 12;
        for (int i = 1; i < 13; i++) {
            float textSize = textPaint.measureText(i + "", 0, (i + "").length());
            double deltaAngle = singleAngle * i;
            double y = centerY - radius * Math.cos(deltaAngle)+textSize/2;
            double x = centerX + radius * Math.sin(deltaAngle)-textSize/2;

            canvas.drawText(i + "", (float) x, (float) y, textPaint);

        }

//        画时针，分针，秒针：
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR);
        float fhour =  hour+ minute / 60f;


//        时针
        pointerPaint.setStrokeCap(Paint.Cap.ROUND);
        pointerPaint.setStrokeWidth(20);
        canvas.drawLine(centerX, centerY, (float) (centerX + radius * 0.45 * Math.sin(Math.toRadians(fhour * 30))), (float) (centerY - (radius * 0.5 * Math.cos(Math.toRadians(fhour * 30)))), pointerPaint);


//        分针
        pointerPaint.setStrokeWidth(10);
        canvas.drawLine(centerX, centerY, (float) (centerX + radius * 0.6 * Math.sin(Math.toRadians(minute * 6))), (float) (centerY - (radius * 0.8 * Math.cos(Math.toRadians(minute * 6)))), pointerPaint);

//        秒针
        pointerPaint.setStrokeWidth(3);
        canvas.drawLine(centerX, centerY, (float) (centerX + radius * 0.8 * Math.sin(Math.toRadians(second * 6))), (float) (centerY - (radius * 0.8 * Math.cos(Math.toRadians(second * 6)))), pointerPaint);

//        每隔1秒重绘刷新一次；
        postInvalidateDelayed(1000);
    }


}
