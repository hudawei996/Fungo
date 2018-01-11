package com.pinger.repertory.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pinger.baselib.base.BaseActivity;
import com.pinger.repertory.R;

/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:27
 */

public class CustomWidgetActivity extends BaseActivity {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_custom_widget;
    }

    @Override
    protected void initView() {
        setActionBar(getString(R.string.widget_custom),true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_view:
                startActivity(CustomViewActivity.class);
                break;
        }
    }
}
