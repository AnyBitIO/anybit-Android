package kualian.dc.deal.application.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.bean.WebRequestBean;
import kualian.dc.deal.application.bean.WebResponse;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.web.chromeclient.WebChromeClientImpl;
import kualian.dc.deal.application.web.client.WebMarketClientImpl;
import kualian.dc.deal.application.web.route.RouteKeys;
import kualian.dc.deal.application.web.route.Router;

/**
 *
 */

public class WebDelegateAgree extends SourceDelegate {
    private LinearLayoutCompat linearLayoutCompat;
    private IPageLoadListener mIPageLoadListener = null;
    private WebView webView;
    public static WebDelegateAgree getInstance(String title) {
        final Bundle args = new Bundle();
        args.putString(RouteKeys.TITLE.name(), title);
        final WebDelegateAgree delegate = new WebDelegateAgree();
        delegate.setArguments(args);
        return delegate;
    }
    @Override
    protected void onEvent() {
        //
        requestNetwork();

    }
    private void requestNetwork() {
        WebRequestBean webRequestBean = new WebRequestBean();
        WebRequestBean.HeaderBean headerBean = new WebRequestBean.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.WEB_PAGE);
        WebRequestBean.BodyBean bodyBean = new WebRequestBean.BodyBean();
        bodyBean.setPageType(Constants.WEB_AGREEMENT);
        webRequestBean.setHeader(headerBean);
        webRequestBean.setBody(bodyBean);
        presenter.queryServiceData(new Gson().toJson(webRequestBean), Constants.WEB_PAGE);
    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_web;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        findToolBar(rootView);

        toolTitle.setText(R.string.agree_deals);
        toolTitle.setSingleLine();
        toolTitle.setEllipsize(TextUtils.TruncateAt.END);
        toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });


        linearLayoutCompat = rootView.findViewById(R.id.contain);
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getContext());
        webView.setLayoutParams(layoutParams);
        linearLayoutCompat.addView(webView);
        initWebView(webView);
    }


    public void initWebView(WebView webView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //不能横向滚动
        webView.setHorizontalScrollBarEnabled(false);
        //不能纵向滚动
        webView.setVerticalScrollBarEnabled(false);
        //允许截图
        webView.setDrawingCacheEnabled(true);
        //屏蔽长按事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        //初始化WebSettings
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        final String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + "Latte");
        //隐藏缩放控件
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        //禁止缩放
        settings.setSupportZoom(false);
        //文件权限
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        //缓存相关
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        initWebViewClient(webView);
        //initWebView(webView);
    }


    public void initWebViewClient(WebView webView) {

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                if (url.contains("tel:")) {
                    callPhone(getContext(), url);
                    return true;
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                AppLoader.showLoading(getContext());
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                AppLoader.stopLoading();

            }
        });
    }


    @Override
    public void getServiceData(ResponseData response, String tag) {
        super.getServiceData(response, tag);
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebResponse webResponse = null;
                try {
                    webResponse = new Gson().fromJson(response.getResponse(), WebResponse.class);
                } catch (Exception ignored) {
                }
                if (webResponse != null && webResponse.getData() != null) {
                    String webUrl = webResponse.getData().getPageUrl();
                    webView.loadUrl(webUrl);
                    //Router.getInstance().loadPage(WebDelegateAgree.this, webUrl);

                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        linearLayoutCompat.removeAllViews();
    }

    private void callPhone(Context context, String uri) {
        final Intent intent = new Intent(Intent.ACTION_DIAL);
        final Uri data = Uri.parse(uri);
        intent.setData(data);
        ContextCompat.startActivity(context, intent, null);
    }
}
