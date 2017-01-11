package demo.com.customviewdemo.customview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Alv_chi.
 */
public class MyDraggableRelativeLayout extends RelativeLayout {


    private static final String TAG = "Test";
    private int childCount;
    private float downY;
    private float moveY;

    private View firstContentView;

    private boolean isMoved;
    private View visibleContentView;
    private ArrayList<Rect> rects;
    private ArrayList<View> views;
    private boolean isListlView;
    private boolean isNeedMoved;


    public MyDraggableRelativeLayout(Context context) {
        this(context, null);
    }

    public MyDraggableRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDraggableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyDraggableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();//当系统加载完Xml就会调用此方法；

        childCount = getChildCount();
        Log.e(TAG, "onFinishInflate: childCount=" + childCount);
        if (childCount > 0) {
            firstContentView = getChildAt(0);//获取子view：
            if (childCount == 1) {
//                当只有一个子view时（没有背景View），该子View就是可被拉拽的子View；
                visibleContentView = this.firstContentView;
            } else {
//                当有多个子View时，默认第二个子view可拉拽子View，
//                其实当被拉拽时，除了第一个子view（背景子View）不会移动，其他所有子view都会移动；
//                因为我这里继承的是relativeLayout所以我取第一个，要是继承FrameLAyout的话，
//                  应该是取最后一个子View；
                visibleContentView = getChildAt(1);
            }

//            此处做法的原因是LIstVIew的getScrollView()方法获取到的值一直是0，
//            所以要计算出当前listView是否到达顶端，就要另辟蹊径；
            if (visibleContentView instanceof ListView) {
                isListlView = true;
            } else {
                isListlView = false;
            }

        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (visibleContentView == null) {
            return;
        }
        rects = new ArrayList<>();
        views = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
//        将原来每个view对应的位置纪录下来，用于下面view恢复原来位置用的；
            View childView = getChildAt(i);
            views.add(childView);
            rects.add(new Rect(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom()));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();//纪录按下位置
                break;
            case MotionEvent.ACTION_MOVE:

                if (visibleContentView != null) {
                    moveY = ev.getY();//纪录移动位置
                    float deltaY = moveY - downY;//计算Y变化值；

                    int offset = (int) (deltaY * 0.7);//变化Y值乘以阻尼系数为偏移量；

                    if (isListlView) {
                        ListView listView = (ListView) this.visibleContentView;
                        int childCount = listView.getChildCount();
                        if (childCount > 0) {
//                            判断当前的listView是否滑动到顶部；
                            isNeedMoved = listView.getFirstVisiblePosition() == 0 && deltaY > 0;
                        }
                    } else {
                        isNeedMoved = visibleContentView.getScrollY() == 0 && deltaY > 0;
                    }

                    if (isNeedMoved) {
//                       当子View数量大于1时，背景子view（第一个子View）不移动，移动其他所有子view；
                        for (int i = 0; i < childCount; i++) {
                            if (childCount > 1 && i == 0) continue;//当子View数为1时，该子view也动；

                            View childView = views.get(i);
                            Rect rect = rects.get(i);
//                            移动所要移动的子View；
                            childView.layout(rect.left, rect.top + offset, rect.right, rect.bottom + offset);
                        }

                        isMoved = true;//标记一下，移动了；
                    }
                }


                break;
            case MotionEvent.ACTION_UP:
                if (isMoved) {

                    for (int i = 0; i < childCount; i++) {
                        if (childCount > 1 && i == 0) continue;
                        final Rect rectII = rects.get(i);
                        final View childView = views.get(i);

//                        此处开线程的目的是，让动画与移动同步不延迟；
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 定义动画，准备动画；
                                        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, childView.getTop(), rectII.top);
                                        translateAnimation.setDuration(500);
                                        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                                        translateAnimation.start();

                                        childView.setAnimation(translateAnimation);
//                                        contentView回复原位置；
                                        childView.layout(rectII.left, rectII.top, rectII.right, rectII.bottom);

                                    }
                                });
                            }
                        }).start();


                    }

                    isMoved = false;//标记一下恢复原来的位置了；
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
