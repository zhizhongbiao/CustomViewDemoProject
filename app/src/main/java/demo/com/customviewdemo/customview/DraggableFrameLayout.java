package demo.com.customviewdemo.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

/**
 * Created by Alv_chi on 2016/12/24.
 */
public class DraggableFrameLayout extends FrameLayout {


    private int childCount;
    private float downY;
    private float moveY;
    private float deltaY;

    private View contentView;

    private boolean isMoved;


    public DraggableFrameLayout(Context context) {
        this(context, null);
    }

    public DraggableFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DraggableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        childCount = getChildCount();
        if (childCount > 0) {
            contentView = getChildAt(0);//获取子view：
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = ev.getY();
                deltaY = moveY - downY;//变化的Y值
                float offset = deltaY*0.7f;//乘以阻尼系数之后的Y偏移值；

                if (contentView!=null&&contentView.getScrollY() == 0 && deltaY > 0) {
                    scrollTo(0, -(int) ( contentView.getScrollY()+offset));//进行偏移
                    isMoved = true;//标志移动了
                    return true;//防止与子view（ListView，ScrollView等可滚动View）滑动冲突

                }
                break;
            case MotionEvent.ACTION_UP:
                if (isMoved) {
//                    定义动画
                    int scrollY = getScrollY();
                    Log.e("Test", "dispatchTouchEvent: scrollY="+scrollY );
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -scrollY, 0);
                    translateAnimation.setDuration(300);
                    translateAnimation.setInterpolator(new LinearInterpolator());
                    translateAnimation.start();
                    contentView.setAnimation(translateAnimation);
//                    contentView回复原位置；

                    scrollTo(0,0);
                    isMoved = false;

                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
