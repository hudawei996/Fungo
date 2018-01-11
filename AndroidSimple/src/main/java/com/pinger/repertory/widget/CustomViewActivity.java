package com.pinger.repertory.widget;

import android.view.View;

import com.pinger.baselib.base.BaseActivity;
import com.pinger.repertory.R;
import com.pinger.repertory.widget.banner.BannerViewActivity;
import com.pinger.repertory.widget.falling.FallingViewActivity;

/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:28
 */

public class CustomViewActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_custom_view;
    }

    @Override
    protected void initView() {
        setActionBar(getString(R.string.widget_view_custom), true);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_falling:
                startActivity(FallingViewActivity.class);
                break;
            case R.id.btn_banner:
                startActivity(BannerViewActivity.class);
                break;
        }
    }
}
