package kualian.dc.deal.application.web.client;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.web.IPageLoadListener;
import kualian.dc.deal.application.web.WebDelegate;
import kualian.dc.deal.application.web.route.Router;


/**
 *
 */

public class WebViewClientImpl extends WebViewClient {

    private final WebDelegate DELEGATE;
    private IPageLoadListener mIPageLoadListener = null;
    private static final Handler HANDLER = Latte.getHandler();
    private boolean isLoad;
    public void setPageLoadListener(IPageLoadListener listener) {
        this.mIPageLoadListener = listener;
    }

    public WebViewClientImpl(WebDelegate delegate,boolean isLoad) {
        this.DELEGATE = delegate;
        this.isLoad=isLoad;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
     /*   if (url.startsWith("http://") || url.startsWith("https://")) {
            view.loadUrl(url);
            view.stopLoading();
            return true;
        }*/
        view.loadUrl(url);
        return Router.getInstance().handleWebUrl(DELEGATE, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (mIPageLoadListener != null) {
            mIPageLoadListener.onLoadStart();
        }
        if (isLoad){
            AppLoader.showLoading(view.getContext());
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
       // super.onReceivedSslError(view, handler, error);
        handler.proceed();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (mIPageLoadListener != null) {
            mIPageLoadListener.onLoadEnd();
        }
        if (isLoad){
            AppLoader.stopLoading();
        }
        /*HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppLoader.stopLoading();
            }
        }, 1000);*/
    }
}
