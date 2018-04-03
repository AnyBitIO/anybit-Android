package kualian.dc.deal.application;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.zhouyou.http.RxHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.bitcoinj.crypto.MnemonicCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.SystemInfoUtils;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.wallet.coins.BitcoinMain;
import kualian.dc.deal.application.wallet.coins.UbMain;
import okhttp3.OkHttpClient;


/**
 * @author John L. Jegutanis
 * @author Andreas Schildbach
 */

public class WalletApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        WalletTool.init(this);
        Latte.init(this);
        Latte.getConfigurator().configure();
        //ChangeLanguageHelper.init(this);
        mContext = getApplicationContext();
        if (MnemonicCode.INSTANCE == null) {
            try {
                MnemonicCode.INSTANCE = new MnemonicCode();
            } catch (IOException e) {
                throw new RuntimeException("Could not set MnemonicCode.INSTANCE", e);
            }
        }
        String random = KeyUtil.getRandom();
        RxHttp.init(this);


        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .readTimeout(30000L, TimeUnit.MILLISECONDS)
                //.addInterceptor(new LoggerInterceptor("TAG"))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);

    }

    public static Context getContext() {
        return mContext;
    }
}