package com.leibo.repertory.main;

import android.support.annotation.NonNull;
import android.view.View;

import com.leibo.baselib.base.BaseActivity;
import com.leibo.repertory.player.PlayerMainActivity;
import com.leibo.repertory.recycler.RecyclerPageActivity;
import com.leibo.repertory.widget.CustomViewActivity;
import com.leibo.repertory.R;

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
