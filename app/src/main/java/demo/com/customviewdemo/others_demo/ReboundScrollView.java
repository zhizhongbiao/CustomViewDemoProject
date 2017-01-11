package demo.com.customviewdemo.others_demo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ScrollView;

/**
 * Created by Alv_chi on 2016/12/19.
 */


public class ReboundScrollView extends ScrollView {
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 500;
    private Context mContext;
    private int mMaxYOverscrollDistance;

    public ReboundScrollView(Context context) {
        super(context);
        mContext = context;
        initBounceScrollView();
    }

    public ReboundScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBounceScrollView();
    }

    public ReboundScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initBounceScrollView();
    }

    private void initBounceScrollView() {
        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        final float density = metrics.density;

        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //这块是关键性代码
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
    }
}

