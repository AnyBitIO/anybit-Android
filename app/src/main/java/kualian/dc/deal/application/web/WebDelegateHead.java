package kualian.dc.deal.application.web;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import java.util.NavigableMap;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.bean.WebRequestBean;
import kualian.dc.deal.application.bean.WebResponse;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.web.chromeclient.WebChromeClientImpl;
import kualian.dc.deal.application.web.client.WebViewClientImpl;
import kualian.dc.deal.application.web.route.RouteKeys;
import kualian.dc.deal.application.web.route.Router;

/**
 *
 */

public class WebDelegateHead extends WebDelegate {
    private LinearLayoutCompat linearLayoutCompat;
    private IPageLoadListener mIPageLoadListener = null;
    private String webUrl;
    private boolean isLoad;
    public static WebDelegateHead getInstance(String url, String title,boolean isLoad) {
        final Bundle args = new Bundle();
        args.putString(RouteKeys.URL.name(), url);
        args.putString(RouteKeys.TITLE.name(), title);
        args.putBoolean(RouteKeys.LOAD.name(),isLoad);
        final WebDelegateHead delegate = new WebDelegateHead();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    protected void onEvent() {
        //
        CallbackManager.getInstance().addCallback(CallbackType.WEB_TAG, new IGlobalCallback<Boolean>() {
            @Override
            public void executeCallback(@Nullable Boolean args) {
                if (args!=null&&args){
                    requestNetwork();
                }
            }
        });
        if (getArguments().getBoolean(RouteKeys.LOAD.name())){
            requestNetwork();
            return;
        }
        if (!TextUtils.isEmpty(SpUtil.getInstance().getWalletID())){
            requestNetwork();
        }

    }
    private void requestNetwork() {
            WebRequestBean webRequestBean = new WebRequestBean();
            WebRequestBean.HeaderBean headerBean = new WebRequestBean.HeaderBean(KeyUtil.getRandom());
            headerBean.setTrancode(Constants.WEB_PAGE);
            WebRequestBean.BodyBean bodyBean = new WebRequestBean.BodyBean();
            bodyBean.setPageType(getUrl());
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

    public void setPageLoadListener(IPageLoadListener listener) {
        this.mIPageLoadListener = listener;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        findToolBar(rootView);
        if (getUrl().equals(Constants.WEB_MARKET)) {
            toolBack.setVisibility(View.GONE);
        }
        isLoad=getArguments().getBoolean(RouteKeys.LOAD.name());
        toolTitle.setText(getArguments().getString(RouteKeys.TITLE.name()));
        toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });


        linearLayoutCompat = rootView.findViewById(R.id.contain);
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        WebView webView = getWebView();
        webView.setLayoutParams(layoutParams);
        linearLayoutCompat.addView(webView);
        //用原生的方式模拟Web跳转并进行页面加载
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
        LogUtils.i("Load "+isLoad);
        final WebViewClientImpl client = new WebViewClientImpl(this,getArguments().getBoolean(RouteKeys.LOAD.name()));
        client.setPageLoadListener(mIPageLoadListener);
        return client;
    }

    @Override
    public WebChromeClient initWebChromeClient() {
        return new WebChromeClientImpl();
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
                    webUrl = webResponse.getData().getPageUrl();
                    Router.getInstance().loadPage(WebDelegateHead.this, webUrl);

                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        linearLayoutCompat.removeAllViews();
    }

}
