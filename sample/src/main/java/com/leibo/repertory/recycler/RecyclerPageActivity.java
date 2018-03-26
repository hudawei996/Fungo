package com.leibo.repertory.recycler;

import android.view.View;

import com.leibo.baselib.base.BaseActivity;
import com.leibo.repertory.R;

/**
 * @author Pinger
 * @since 2018/1/13 0013 上午 11:09
 */

public class RecyclerPageActivity extends BaseActivity {
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_recycler_page;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_recycler_view) {
            startActivity(RecyclerViewActivity.class);
        }
    }

    @Override
    protected void initView() {
        setActionBar(getString(R.string.recycler_page), true);
    }
}
