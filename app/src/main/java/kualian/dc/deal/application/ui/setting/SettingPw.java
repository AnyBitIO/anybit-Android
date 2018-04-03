package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.database.WalletTable;
import kualian.dc.deal.application.util.AesUtil;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by idmin on 2018/2/28.
 */

public class SettingPw extends SourceDelegate {
    private static final String TAG = "SettingPw";
    private TextView mBack, mTitle, mNext;
    private AutoCompleteTextView oldPw, newPw, surePw;
    private boolean isLoginPw;
    private WalletDao walletDao;
    public static SettingPw getInstance(boolean isLoginPw) {
        SettingPw settingPw = new SettingPw();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TAG, isLoginPw);
        settingPw.setArguments(bundle);
        return settingPw;
    }

    @Override
    public Object setLayout() {
        return R.layout.setting_pw;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        mBack = rootView.findViewById(R.id.toolbar_back);
        mTitle = rootView.findViewById(R.id.toolbar_title);
        mNext = rootView.findViewById(R.id.toolbar_next);
        walletDao=new WalletDao();
        oldPw = rootView.findViewById(R.id.setting_old_pw);
        newPw = rootView.findViewById(R.id.setting_new_pw);
        surePw = rootView.findViewById(R.id.setting_sure_pw);
        setOnClickViews(mBack, mNext);

    }

    @Override
    protected void onEvent() {
        super.onEvent();
        isLoginPw = getArguments().getBoolean(TAG);
        mNext.setText(R.string.sure);
        mNext.setVisibility(View.VISIBLE);
        mNext.setCompoundDrawables(null, null, null, null);
        if (isLoginPw) {
            mTitle.setText(getResources().getString(R.string.setting_login_pw_short));
        } else {
            mTitle.setText(getResources().getString(R.string.setting_pw_short));
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.toolbar_back) {
            CommonUtil.hideSoftInput(_mActivity);
            _mActivity.onBackPressed();
        } else {
            if (isLoginPw) {
                if (KeyUtil.getPwMessage(oldPw.getText().toString()).equals(SpUtil.getInstance().getLoginPw())) {
                    if (newPw.getText().toString().equals(surePw.getText().toString())) {
                        if (newPw.getText().toString().length() != 6) {
                            RxToast.showToast(getResources().getString(R.string.setting_pw_six));
                        } else {
                            SpUtil.getInstance().setLoginPw(surePw.getText().toString());
                            walletDao.update(WalletTable.WALLET_LOGIN_PW,KeyUtil.getPwMessage(surePw.getText().toString()), SpUtil.getInstance().getWalletID());
                            CommonUtil.hideSoftInput(_mActivity);
                            _mActivity.onBackPressed();
                        }
                    } else {
                        RxToast.showToast(getResources().getString(R.string.setting_error_two));
                    }
                } else {
                    RxToast.showToast(getResources().getString(R.string.setting_error_pw));
                }

            } else {
                if (KeyUtil.getPwMessage(oldPw.getText().toString()).equals(SpUtil.getInstance().getWalletPw())) {
                    if (newPw.getText().toString().equals(surePw.getText().toString())) {
                        if (newPw.getText().toString().length() != 6) {
                            RxToast.showToast(getResources().getString(R.string.setting_pw_six));
                        } else {
                            SpUtil.getInstance().setWalletPw(surePw.getText().toString());
                            //更新交易密码
                            SpUtil.getInstance().setWalletSend(surePw.getText().toString(), SpUtil.getInstance().getWalletSend(oldPw.getText().toString()));
                            walletDao.update(WalletTable.WALLET_TRADE_PW,KeyUtil.getPwMessage(surePw.getText().toString()), SpUtil.getInstance().getWalletID());
                            CommonUtil.hideSoftInput(_mActivity);
                            _mActivity.onBackPressed();
                        }
                    } else {
                        RxToast.showToast(getResources().getString(R.string.setting_error_two));
                    }
                } else {
                    RxToast.showToast(getResources().getString(R.string.setting_error_pw));
                }
            }
        }
    }
}
