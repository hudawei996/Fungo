package com.pinger.repertory.main;

import android.support.annotation.NonNull;
import android.view.View;

import com.pinger.baselib.base.BaseActivity;
import com.pinger.repertory.R;
import com.pinger.repertory.player.PlayerMainActivity;
import com.pinger.repertory.recycler.RecyclerPageActivity;
import com.pinger.repertory.widget.CustomViewActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btn_widget:
                startActivity(CustomViewActivity.class);
                break;
            case R.id.btn_recycler:
                startActivity(RecyclerPageActivity.class);
                break;
            case R.id.btn_player:
                startActivity(PlayerMainActivity.class);
                break;
        }
    }
}
