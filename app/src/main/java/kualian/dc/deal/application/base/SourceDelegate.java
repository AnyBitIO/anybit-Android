package kualian.dc.deal.application.base;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ConnectException;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.presenter.home.HomeContract;
import kualian.dc.deal.application.presenter.impl.DataPresenter;
import kualian.dc.deal.application.ui.MainDelegate;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.RxToast;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by zheng on 2017/12/15.
 */

public abstract class SourceDelegate<M extends BaseModel, P extends BasePresenter> extends PermissionCheckerDelegate<M, P> implements HomeContract.View, View.OnClickListener {
    protected TextView toolBack, toolTitle, toolNext;
    protected ResponseData responseData;
    protected String sign;

    @SuppressWarnings("unchecked")
    public <T extends MainDelegate> T getParentDelegate() {
        return (T) getParentFragment();
    }

    @Override
    protected void lazyFetchData() {
        presenter = new DataPresenter(this);
    }

    @Override
    public void showErrorWithStatus(Throwable throwable) {
        if (throwable instanceof ConnectException) {
            RxToast.showToast(getString(R.string.view_network_error));
        } else {
            RxToast.showToast(getString(R.string.service_fail));
        }
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    public void setOnClickViews(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    public void findToolBar(View rootView) {
        toolBack = rootView.findViewById(R.id.toolbar_back);
        toolTitle = rootView.findViewById(R.id.toolbar_title);
        toolNext = rootView.findViewById(R.id.toolbar_next);
        setOnClickViews(toolBack, toolNext);
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {
    }
    private static final long WAIT_TIME = 1000L;
    private long TOUCH_TIME = 0;
    @Override
    public void start(ISupportFragment toFragment) {
        if (System.currentTimeMillis() - TOUCH_TIME > WAIT_TIME) {
            TOUCH_TIME = System.currentTimeMillis();
            super.start(toFragment);
        } else {
            TOUCH_TIME = System.currentTimeMillis();
        }
    }
}
