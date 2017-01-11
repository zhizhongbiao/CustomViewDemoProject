package demo.com.customviewdemo.customview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by Alv_chi on 2016/12/19.
 */
public class RebounceScrollViewCopy extends ScrollView {

    private static final String TAG = "test";
    private View contentView;
    private float downY;
    private float moveY;
    private Rect rect;
    private boolean isMoved;

    public RebounceScrollViewCopy(Context context) {
        this(context, null);
    }

    public RebounceScrollViewCopy(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RebounceScrollViewCopy(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RebounceScrollViewCopy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    protected void onFinishInflate() {

        /**
         * onFinishInflate 当View中所有的子控件均被映射成xml后触发
         * F inalize inflating a view from XML.  This is called as the last phase
         * of inflation, after all child views have been added
         */
        super.onFinishInflate();
        if (getChildCount() > 0) {
            contentView = getChildAt(0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        该动作要在父类执行完onLayout()方法之后执行；
        if (contentView == null) return;
//        用一矩形，记录contentView的原始位置：
        rect = new Rect(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (contentView != null) {
                    moveY = ev.getY();
                    float deltaY = moveY - downY;
                    Log.e(TAG, "dispatchTouchEvent: deltaY="+deltaY );
                    int offset = (int) (deltaY * 0.5);
                    int scrollY = getScrollY();//获取滚动在屏幕外顶部到屏幕可见的距离；
                    if (scrollY == 0 && deltaY > 0) {//计算是否到ScrollView的顶部，以及判断是否向下拉；
//                       移动ContentView,并标记移动了
                        contentView.layout(rect.left, rect.top + offset, rect.right, rect.bottom + offset);

                        isMoved = true;
                    } else if (scrollY + getHeight() == contentView.getHeight() && deltaY < 0) {//计算是否到ScrollVIew底部，以及判断是否向上拉；
//                         移动ContentView,并标记移动了
                        contentView.layout(rect.left, rect.top + offset, rect.right, rect.bottom + offset);

                        isMoved = true;
                    }
                }


                break;
            case MotionEvent.ACTION_UP:
                if (isMoved) {
//                    定义动画
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, contentView.getTop(), rect.top);
                    translateAnimation.setDuration(800);
                    translateAnimation.setInterpolator(new BounceInterpolator());
                    translateAnimation.start();
                    contentView.setAnimation(translateAnimation);
//                    contentView回复原位置；
                    contentView.layout(rect.left, rect.top, rect.right, rect.bottom);

                    isMoved = false;
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
