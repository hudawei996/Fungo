package com.pinger.repertory.banner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pinger.repertory.R;
import com.pinger.widght.banner.BannerView;
import com.pinger.widght.banner.holder.BannerHolderCreator;
import com.pinger.widght.banner.holder.BannerViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author pinger
 * @since 2017/11/25 20:45
 */

public class BannerTestActivity extends AppCompatActivity {

    Integer[] mRes = {R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3, R.mipmap.banner4, R.mipmap.banner5};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_test);
        BannerView bannerView = (BannerView) findViewById(R.id.banner_view);

        List<Integer> datas = new ArrayList<>();
        Collections.addAll(datas, mRes);

        bannerView.setBannerPageClickListener(new BannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Toast.makeText(BannerTestActivity.this, "点击了" + position, Toast.LENGTH_LONG).show();
            }
        });

        bannerView.setDuration(3000);
        bannerView.setDelayedTime(3000);
        bannerView.setIndicatorVisible(false);
        bannerView.setUseDefaultDuration(false);
        bannerView.setPages(datas, new BannerHolderCreator() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new AdViewHolder();
            }
        });
        bannerView.start();
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
