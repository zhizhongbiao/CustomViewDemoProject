package demo.com.customviewdemo.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Alv_chi on 2016/12/19.
 */
public class BounceScrollView extends ScrollView {

    private Context mContext;
    private int maxYDistanceByOverScroll;

    public BounceScrollView(Context context) {
        this(context,null);
    }

    public BounceScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BounceScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public BounceScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        maxYDistanceByOverScroll = (int) (mContext.getResources().getDisplayMetrics().density * 200);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxYDistanceByOverScroll, isTouchEvent);
    }
}
