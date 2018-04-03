package kualian.dc.deal.application.web;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.web.chromeclient.WebChromeClientImpl;
import kualian.dc.deal.application.web.client.WebViewClientImpl;
import kualian.dc.deal.application.web.route.RouteKeys;
import kualian.dc.deal.application.web.route.Router;

/**
 *
 */

public class WebDelegateImpl extends WebDelegate {
    private LinearLayoutCompat linearLayoutCompat;
    private IPageLoadListener mIPageLoadListener = null;

    public static WebDelegateImpl getInstance(String url) {
        final Bundle args = new Bundle();
        args.putString(RouteKeys.URL.name(), url);
        final WebDelegateImpl delegate = new WebDelegateImpl();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    protected void onEvent() {

    }


    @Override
    protected BaseView getViewImp() {
        return null;
    }

    @Override
    public Object setLayout() {

        return getWebView();
    }

    public void setPageLoadListener(IPageLoadListener listener) {
        this.mIPageLoadListener = listener;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
       /* findToolBar(rootView);
        toolTitle.setText("UB.com");
        toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
        linearLayoutCompat = rootView.findViewById(R.id.web_contain);
        LinearLayoutCompat.LayoutParams layoutParams=new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        WebView webView = getWebView();
        webView.setLayoutParams(layoutParams);
        linearLayoutCompat.addView(webView);*/
        if (getUrl() != null) {
            //用原生的方式模拟Web跳转并进行页面加载
            Router.getInstance().loadPage(this, getUrl());
        }
    }

    @Override
    public IWebViewInitializer setInitializer() {
        return this;
    }

    @Override
    public WebView initWebView(WebView webView) {
        return new WebViewInitializer().createWebView(webView);
    }

    @Override
    public WebViewClient initWebViewClient() {
        final WebViewClientImpl client = new WebViewClientImpl(this,true);
        client.setPageLoadListener(mIPageLoadListener);
        return client;
    }

    @Override
    public WebChromeClient initWebChromeClient() {
        return new WebChromeClientImpl();
    }

 /*   @Override
    public void onDestroy() {
        super.onDestroy();
        linearLayoutCompat.removeAllViews();
    }*/
}
