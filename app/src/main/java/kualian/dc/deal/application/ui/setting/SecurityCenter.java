package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by idmin on 2018/3/21.
 */

public class SecurityCenter extends SourceDelegate {
    private TextView mTradePw, mLoginPw;
    private Switch mSwitch;

    @Override
    public Object setLayout() {
        return R.layout.delegate_security;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        mTradePw = rootView.findViewById(R.id.setting_trade_pw);
        mLoginPw = rootView.findViewById(R.id.setting_pw);
        findToolBar(rootView);
        toolTitle.setText(R.string.setting_safe);
        mSwitch = rootView.findViewById(R.id.setting_re_login);
        mSwitch.setChecked(SpUtil.getInstance().getIsReLogin());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpUtil.getInstance().setIsReLogin(b);
            }
        });
        setOnClickViews(toolBack, mTradePw, mLoginPw);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.setting_trade_pw:
                start(SettingPw.getInstance(false));
                break;
            case R.id.setting_pw:
                start(SettingPw.getInstance(true));
                break;
            case R.id.toolbar_back:
                _mActivity.onBackPressed();
                break;
        }
    }
}
