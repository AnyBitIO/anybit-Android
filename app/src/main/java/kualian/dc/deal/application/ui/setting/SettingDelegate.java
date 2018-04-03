package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.ui.account.DelegateVerify;
import kualian.dc.deal.application.ui.contact.ContactDelegate;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.web.WebDelegateHead;

/**
 * Created by idmin on 2018/2/10.
 */

public class SettingDelegate extends SourceDelegate {
    private TextView  mine, safe, language, default_coin, about_us, problem, update,contact;
    //private Switch mSwitch;
    private static SettingDelegate instance = null;

    public static SettingDelegate getInstance() {
       /* if (instance == null) {
            instance = new SettingDelegate();
        }*/
        return new SettingDelegate();
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
        return R.layout.delegate_setting;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mine = rootView.findViewById(R.id.setting_mine);
        update = rootView.findViewById(R.id.check_update);
        contact = rootView.findViewById(R.id.contact);
        safe = rootView.findViewById(R.id.setting_safe);
        language = rootView.findViewById(R.id.setting_language);
        default_coin = rootView.findViewById(R.id.setting_coin);
        about_us = rootView.findViewById(R.id.setting_about_us);
        problem = rootView.findViewById(R.id.setting_problem);
        setOnClickViews( mine, safe, language, default_coin, about_us, problem, update,contact);
        mine.setText(SpUtil.getInstance().getWalletName());
        onNameChanged();
    }

    private void onNameChanged() {
        CallbackManager.getInstance().addCallback(CallbackType.ON_NAME_CHANGE, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {
                mine.setText(SpUtil.getInstance().getWalletName());
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
           /* case R.id.setting_pw:
                getParentDelegate().start(SettingPw.getInstance(false));
                break;*/
            case R.id.setting_safe:
                getParentDelegate().start(new SecurityCenter());
                break;
           /* case R.id.setting_problem:
                getParentDelegate().start(SettingPw.getInstance(true));
                break;*/
            case R.id.setting_mine:
                getParentDelegate().start(PersonalCenter.getInstance(SpUtil.getInstance().getWalletName()));
                break;
            case R.id.setting_language:
                getParentDelegate().start(SettingDefault.getInstance(false));
                break;
            case R.id.setting_coin:
                getParentDelegate().start(SettingDefault.getInstance(true));
                break;
            case R.id.setting_about_us:
                getParentDelegate().start(new AboutUsDelegate());
                break;
            case R.id.contact:
                getParentDelegate().start(ContactDelegate.getInstance());
                break;
            case R.id.setting_problem:
                getParentDelegate().start(WebDelegateHead.getInstance(Constants.WEB_PROBLEM,getResources().getString(R.string.setting_problem),true));
                //getParentDelegate().start(new DelegateVerify());
                break;
            case R.id.check_update:
                getParentDelegate().start(new CheckUpdateDelegate());
                break;
            default:
        }
    }
}
