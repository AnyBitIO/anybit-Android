package kualian.dc.deal.application.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import java.util.Locale;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceActivity;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by idmin on 2018/2/12.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CommonUtil.switchLanguage("en");
        setContentView(R.layout.delegate_welcome);
        try {
            PackageManager manager = getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(this.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            SpUtil.getInstance().setDefaultVersion(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, SourceActivity.class));
                finish();
                //start(MainDelegate.getInstance());
            }
        },500);
    }

}
