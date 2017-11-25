package com.pinger.repertory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pinger.repertory.banner.BannerTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.banner_view:
                launch(BannerTestActivity.class);
                break;
        }
    }

    private void launch(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

}
