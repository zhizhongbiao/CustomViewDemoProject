package demo.com.customviewdemo.customview;

        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.util.AttributeSet;
        import android.view.View;

/**
 * Created by Alv_chi on 2016/12/18.
 */
public class MyFiveStarView extends View{

    private Paint paint;
    private double[] arrY;
    private double[] arrX;
    private Path path;
    private double[] X;
    private double[] Y;


    public MyFiveStarView(Context context) {
        this(context,null);
    }

    public MyFiveStarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyFiveStarView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public MyFiveStarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(20);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();
        int centerY = height / 2;
        int centerX = width / 2;
        int radius = (height > width ? width : height) / 4;

        initialPointArr(centerY, centerX, radius);
//      画正五边形：
//        drawGraphic(canvas,arrX,arrY);

//        改变一下数组坐标顺序，就可以画个五角星：
        drawGraphic(canvas,X,Y);
    }

    private void drawGraphic(Canvas canvas,double[] X,double[] Y) {
        path = new Path();


        for (int i = 0; i < 5; i++) {
            if (i==0)
            {
                path.moveTo((float) X[i], (float) Y[i]);

            }else
            {
                path.lineTo((float) X[i], (float) Y[i]);

            }
        }

        path.close();

        canvas.drawPath(path,paint);
    }

    private void initialPointArr(int centerY, int centerX, int radius) {

//        从最高顶点开始，从左往右开始计算各个顶点的坐标；
//        顶点坐标：
        double tX = centerX;
        double tY = centerY - radius;

//        右中顶点坐标：
        double rmY = centerY-radius *Math.sin(Math.toRadians(18));
        double rmX = tX + radius * Math.cos(Math.toRadians(18));

//        右底顶点坐标：
        double rbY = centerY + radius * Math.cos(Math.toRadians(36));
        double rbX = centerX + radius *Math.sin(Math.toRadians(36));;

//        相对应的左边中底顶点坐标为：
        double lmY = rmY;
        double lmX = tX -radius * Math.cos(Math.toRadians(18));

        double lbY = rbY;
        double lbX = centerX - radius *Math.sin(Math.toRadians(36));

//        分别用两数组对应地装起来；
        arrY = new double[]{tY,rmY,rbY,lbY,lmY};
        arrX = new double[]{tX,rmX,rbX,lbX,lmX};
//      改变数组顺序：
        X = new double[]{lbY,tY,rbY,lmY,rmY};
        Y = new double[]{lbX,tX,rbX,lmX,rmX};
    }
}
