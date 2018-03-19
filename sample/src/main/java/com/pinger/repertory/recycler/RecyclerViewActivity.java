package com.pinger.repertory.recycler;

import com.pinger.baselib.base.BaseActivity;
import com.pinger.repertory.R;

/**
 * @author Pinger
 * @since 2018/1/13 0013 上午 11:11
 */

public class RecyclerViewActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_recycler_view;
    }

    @Override
    protected void initView() {
        setActionBar(getString(R.string.recycler_view),true);
    }
}
