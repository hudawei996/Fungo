package com.pinger.repertory.widget.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.pinger.baselib.base.BaseActivity;
import com.pinger.repertory.R;
import com.pinger.widget.view.banner.BannerView;
import com.pinger.widget.view.banner.holder.BannerHolderCreator;
import com.pinger.widget.view.banner.holder.BannerViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author pinger
 * @since 2017/11/25 20:45
 */

public class BannerViewActivity extends BaseActivity {

    private Integer[] mRes = {R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3, R.mipmap.banner4, R.mipmap.banner5};
    private BannerView mBannerView;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_banner_view;
    }

    @Override
    protected void initView() {
        setActionBar(getString(R.string.widget_view_banner), true);
        mBannerView = (BannerView) findViewById(R.id.banner_view);
        mBannerView.setDuration(3000);
        mBannerView.setDelayedTime(3000);
        mBannerView.setIndicatorVisible(false);
        mBannerView.setUseDefaultDuration(false);
    }

    @Override
    protected void initEvent() {
        mBannerView.setBannerPageClickListener(new BannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                showToast("点击了" + position);
            }
        });
    }

    @Override
    protected void initData() {
        List<Integer> datas = new ArrayList<>();
        Collections.addAll(datas, mRes);
        mBannerView.setPages(datas, new BannerHolderCreator() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new AdViewHolder();
            }
        });
        mBannerView.start();
    }

    private class AdViewHolder implements BannerViewHolder<Integer> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            imageView = view.findViewById(R.id.imageView);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            imageView.setImageResource(data);
        }
    }
}
