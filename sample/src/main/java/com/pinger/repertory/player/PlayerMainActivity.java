package com.pinger.repertory.player;

import com.pinger.baselib.base.BaseActivity;
import com.pinger.repertory.R;

/**
 * @author Pinger
 * @since 2018/1/13 0013 上午 11:16
 */

public class PlayerMainActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_player_main;
    }

    @Override
    protected void initView() {
        setActionBar(getString(R.string.video_player), true);
    }
}
