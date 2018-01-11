package com.pinger.repertory;

import android.view.View;

import com.pinger.baselib.base.BaseActivity;
import com.pinger.repertory.widget.CustomWidgetActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_widget:
                startActivity(CustomWidgetActivity.class);
                break;
        }
    }
}
