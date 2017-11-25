package com.pinger.widght.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author pinger
 * @since 2017/11/24 11:51
 * 自定义循环绘制的ViewPager
 */

public class LooperViewPager extends ViewPager {

    private SparseArray<Integer> mChildIndexList;
    private ArrayList<Integer> mChildCenterXAbsList;

    public LooperViewPager(Context context) {
        super(context);
        init();
    }

    public LooperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mChildIndexList = new SparseArray<>();
        mChildCenterXAbsList = new ArrayList<>();
        setClipToPadding(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }


    /**
     * @return 第n个位置的child 的绘制索引
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int n) {
        if (n == 0 || mChildIndexList.size() != childCount) {
            mChildCenterXAbsList.clear();
            mChildIndexList.clear();
            int viewCenterX = getViewCenterX(this);
            for (int i = 0; i < childCount; ++i) {
                int indexAbs = Math.abs(viewCenterX - getViewCenterX(getChildAt(i)));
                // 两个距离相同，后来的那个做自增，从而保持abs不同
                if (mChildIndexList.get(indexAbs) != null) {
                    ++indexAbs;
                }
                mChildCenterXAbsList.add(indexAbs);
                mChildIndexList.append(indexAbs, i);
            }
            Collections.sort(mChildCenterXAbsList);//1,0,2  0,1,2
        }
        // 那个item距离中心点远一些，就先draw它。（最近的就是中间放大的item,最后draw）
        return mChildIndexList.get(mChildCenterXAbsList.get(childCount - 1 - n));
    }

    private int getViewCenterX(View view) {
        int[] array = new int[2];
        view.getLocationOnScreen(array);
        return array[0] + view.getWidth() / 2;
    }
}
