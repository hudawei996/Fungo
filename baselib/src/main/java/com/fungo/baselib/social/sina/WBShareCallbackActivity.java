package com.fungo.baselib.social.sina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fungo.baselib.social.PlatformConfig;
import com.fungo.baselib.social.PlatformType;
import com.fungo.baselib.social.SocialApi;


/**
 * Created by tsy on 16/8/4.
 */
public class WBShareCallbackActivity extends Activity implements IWeiboHandler.Response {

    protected SinaWBHandler mSinaWBHandler = null;

    public WBShareCallbackActivity() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocialApi api = SocialApi.get(this.getApplicationContext());
        this.mSinaWBHandler = (SinaWBHandler) api.getSSOHandler(PlatformType.SINA_WB);
        this.mSinaWBHandler.onCreate(this.getApplicationContext(), PlatformConfig.getPlatformConfig(PlatformType.SINA_WB));

        if(this.getIntent() != null) {
            this.handleIntent(this.getIntent());
        }
    }

    protected final void onNewIntent(Intent paramIntent) {
        super.onNewIntent(paramIntent);
        SocialApi api = SocialApi.get(this.getApplicationContext());
        this.mSinaWBHandler = (SinaWBHandler) api.getSSOHandler(PlatformType.SINA_WB);
        this.mSinaWBHandler.onCreate(this.getApplicationContext(), PlatformConfig.getPlatformConfig(PlatformType.SINA_WB));

        this.handleIntent(this.getIntent());
    }

    protected void handleIntent(Intent intent) {
        this.mSinaWBHandler.onNewIntent(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        this.mSinaWBHandler.onResponse(baseResponse);
        finish();
    }
}
