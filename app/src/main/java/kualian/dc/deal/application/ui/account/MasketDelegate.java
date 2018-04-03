package kualian.dc.deal.application.ui.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.widget.codeView.PayWindow;

/**
 * Created by idmin on 2018/2/10.
 */

public class MasketDelegate extends SourceDelegate{

    private static MasketDelegate instance=null;
    public static MasketDelegate getInstance() {
            instance = new MasketDelegate();
        return    instance;
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
        return R.layout.delegate_news;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }
}
