package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.callback.OnPwListener;
import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.widget.codeView.PayWindow;

/**
 * Created by idmin on 2018/3/14.
 */

public class PrivateKeyDelegate extends SourceDelegate implements OnPwListener {
    private PayWindow payWindow;
    private TextView ub;

    @Override
    public Object setLayout() {
        return R.layout.delegate_key;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        findToolBar(rootView);
        payWindow = new PayWindow(_mActivity, false, this);
        ub = rootView.findViewById(R.id.key_ub);
        toolTitle.setText(R.string.setting_key);
        setOnClickViews(toolBack, ub);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.toolbar_back:
                if (payWindow.isShowing()) {
                    payWindow.dismiss();
                }
                _mActivity.onBackPressed();
                break;
            case R.id.key_ub:
                payWindow.show(view);
                break;

        }
    }

    @Override
    public void getPw(String pw) {
        if (KeyUtil.getPwMessage(pw).equals(SpUtil.getInstance().getWalletPw())) {
            start(KeyDetailDelegate.getInstance(pw));
            payWindow.getCodeView().clear();
            payWindow.dismiss();
        } else {
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.setTranslateAnimationX(payWindow.getContainView());
                    WalletTool.vibrateOnce(_mActivity, 200);
                    payWindow.getCodeView().clear();
                    AppLoader.stopLoading();
                    RxToast.showToast(getResources().getString(R.string.error_pw));
                }
            }, 200);
        }
    }
}
