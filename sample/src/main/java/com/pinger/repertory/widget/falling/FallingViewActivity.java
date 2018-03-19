package com.pinger.repertory.widget.falling;

import com.pinger.baselib.base.BaseActivity;
import com.pinger.repertory.R;
import com.pinger.widget.view.falling.FallingEntity;
import com.pinger.widget.view.falling.FallingView;

/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:51
 */

public class FallingViewActivity extends BaseActivity {
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_falling_view;
    }

    @Override
    protected void initView() {
        setActionBar(getString(R.string.widget_view_falling_snow), true);

        FallingView fallingView = (FallingView) findViewById(R.id.falling_view);

        FallingEntity.Builder builder = new FallingEntity.Builder(getResources().getDrawable(R.mipmap.ic_snow));
        FallingEntity fallingEntity = builder
                .setSpeed(7, true)
                .setSize(50, 50, true)
                .setWind(5, true, true)
                .build();

        fallingView.addFallEntity(fallingEntity, 100);
    }
}
