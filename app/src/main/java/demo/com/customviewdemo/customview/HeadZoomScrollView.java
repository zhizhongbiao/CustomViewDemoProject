package demo.com.customviewdemo.customview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by Alv_chi
 */
public class HeadZoomScrollView extends ScrollView {

    private static final String TAG = "Test";
    private View zoomHeadView;
    private float mDownY;
    private float mMoveY;
    private float mDeltaY;
    private boolean isZoomed;

    private float zoomHeight;
    private double zoomWindth;

    private float ratio = 0.4f;
    private int measuredWidth;
    private int measuredHeight;



    public HeadZoomScrollView(Context context) {
        this(context, null);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount > 0) {
            View contentView = getChildAt(0);
            if (contentView != null && contentView instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) contentView;//ScroView中只能包含一个View；
                int childNum = vg.getChildCount();
                if (childNum > 0) {
                    zoomHeadView = vg.getChildAt(0);

                }
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (measuredWidth <= 0 || measuredHeight <= 0) {
//            获取测量到的可变大得View原始参数
            measuredWidth = zoomHeadView.getMeasuredWidth();
            measuredHeight = zoomHeadView.getMeasuredHeight();

        }
        if (zoomHeadView == null || measuredWidth <= 0 || measuredHeight <= 0) {
            return super.onTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = ev.getY();
                mDeltaY = mMoveY - mDownY;
                if (mDeltaY > 10 && getScrollY() == 0) {
                    zoom(mDeltaY);
                    isZoomed = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isZoomed) {

                    replyView();
                }
                break;

        }

        return super.dispatchTouchEvent(ev);
    }


    private void zoom(float factor) {
//         通过改变参数改变view的大小：
        ViewGroup.LayoutParams layoutParams = zoomHeadView.getLayoutParams();

        zoomHeight = measuredHeight + factor * ratio;
        zoomWindth = measuredWidth + factor * ratio;
        layoutParams.height = (int) zoomHeight;
        layoutParams.width = (int) zoomWindth;

        zoomHeadView.setLayoutParams(layoutParams);

    }


    private void replyView() {
        float delta = zoomHeadView.getMeasuredWidth() - measuredHeight;
        // 设置动画，由delta * ratio变到0过程：
        ValueAnimator anim = ObjectAnimator.ofFloat(delta * ratio, 0.0F).setDuration((long) (delta * ratio));

//        添加一个过程更新监听器：
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //逐帧拿到数据，逐帧缩小
                zoom((Float) animation.getAnimatedValue());
            }
        });

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isZoomed = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();

    }
}
