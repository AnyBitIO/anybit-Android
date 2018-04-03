package kualian.dc.deal.application.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.MySupportFragment;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.bean.UpdateRequestBean;
import kualian.dc.deal.application.bean.UpdateResponseBean;
import kualian.dc.deal.application.callback.CustomTabEntity;
import kualian.dc.deal.application.bean.TabEntity;
import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.ui.account.WalletDelegate;
import kualian.dc.deal.application.ui.news.NewsDelegate;
import kualian.dc.deal.application.ui.setting.SettingDelegate;
import kualian.dc.deal.application.ui.create.RestoreDelegate;
import kualian.dc.deal.application.ui.create.SeedDelegate;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.web.WebDelegateAgree;
import kualian.dc.deal.application.web.WebDelegateHead;
import kualian.dc.deal.application.widget.CommonTabLayout;
import kualian.dc.deal.application.widget.codeView.CodeView;
import kualian.dc.deal.application.widget.codeView.KeyboardView;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by idmin on 2018/2/10.
 */

public class MainDelegate extends SourceDelegate {
    private CommonTabLayout commonTabLayout;
    private ArrayList<MySupportFragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private boolean isCanShow = true;
    private int[] mIconUnselectIds = {
            R.mipmap.wallet_gray, R.mipmap.btn_defaultstate_hangqing, R.mipmap.btn_defaultstate_zixun, R.mipmap.btn_defaultstate_shezhi};
    private int[] mIconSelectIds = {
            R.mipmap.wallet, R.drawable.btn_defaultstate_hangqing, R.drawable.btn_defaultstate_zixun,  R.drawable.btn_defaultstate_shezhi};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    public static MainDelegate getInstance() {
        return new MainDelegate();
    }

    @Override
    protected void onEvent() {
        checkUpdateNet();
    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }
    private void checkUpdateNet() {
        if (!TextUtils.isEmpty(SpUtil.getInstance().getWalletID())){
            UpdateRequestBean requestBean = new UpdateRequestBean();
            UpdateRequestBean.HeaderBean headerBean = new UpdateRequestBean.HeaderBean(KeyUtil.getRandom());
            headerBean.setTrancode(Constants.update_query);
            requestBean.setHeader(headerBean);
            UpdateRequestBean.BodyBean bodyBean=new UpdateRequestBean.BodyBean();
            requestBean.setBody(bodyBean);
            presenter.queryServiceData(new Gson().toJson(requestBean), Constants.update_query);
        }
    }
    @Override
    protected void lazyFetchData() {
        if (findChildFragment(WalletDelegate.class) == null) {
            mTitles = getResources().getStringArray(R.array.sort_item);
            mFragments.add(WalletDelegate.getInstance());
            mFragments.add(WebDelegateHead.getInstance(Constants.WEB_MARKET,getResources().getString(R.string.market),false));
            mFragments.add(NewsDelegate.getInstance());
           // mFragments.add(ContactDelegate.getInstance());
            mFragments.add(SettingDelegate.getInstance());
            for (int i = 0; i < mTitles.length; i++) {
                mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
            }
            commonTabLayout.setTabData(mTabEntities, _mActivity, R.id.fragment_contain, mFragments, true);
            commonTabLayout.setCurrentTab(0);
            final ISupportFragment[] delegateArray = mFragments.toArray(new ISupportFragment[4]);
            getSupportDelegate().loadMultipleRootFragment(R.id.fragment_contain, 0, delegateArray);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.main_page_layout;
    }

    View view, root;

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        root = rootView;
        commonTabLayout = rootView.findViewById(R.id.common_tab);
        if (SpUtil.getInstance().getWalletPw() == null) {
            ViewStub viewStub = rootView.findViewById(R.id.view_stub);
            viewStub.setLayoutResource(R.layout.delegate_create_wallet);
            view = viewStub.inflate();
            TextView restore = (TextView) view.findViewById(R.id.account_restore);
            TextView create = (TextView) view.findViewById(R.id.account_create);
            setOnClickViews(restore, create);
        } else {
            if (SpUtil.getInstance().getIsReLogin()) {
                ViewStub viewStub = rootView.findViewById(R.id.view_stub);
                viewStub.setLayoutResource(R.layout.delegate_login);
                view = viewStub.inflate();
                final KeyboardView keyboardView = view.findViewById(R.id.password_input);
                CodeView codeView = view.findViewById(R.id.password_view);
                LinearLayoutCompat contain=view.findViewById(R.id.code_contain);
                codeView.setShowType(CodeView.SHOW_TYPE_PASSWORD);
                codeView.setLength(6);
                keyboardView.setCodeView(codeView);
                codeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keyboardView.show();
                    }
                });
                codeView.setListener(new CodeView.Listener() {
                    @Override
                    public void onValueChanged(String value) {

                    }

                    @Override
                    public void onComplete(String value) {
                        // TODO: 2017/2/5
                        if (KeyUtil.getPwMessage(value).equals(SpUtil.getInstance().getLoginPw())) {
                            if (view != null) {
                                view.setVisibility(View.GONE);
                            }
                        } else {
                            Latte.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    CommonUtil.setTranslateAnimationX(contain);
                                    WalletTool.vibrateOnce(_mActivity, 200);
                                    codeView.clear();
                                    RxToast.showToast(getResources().getString(R.string.error_pw));
                                }
                            },200);

                        }
                    }
                });
                view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isCanShow = false;
                        start(RestoreDelegate.newInstance(null));
                    }
                });
            }
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.account_restore) {
            start(RestoreDelegate.newInstance(null));
        } else {
            start(SeedDelegate.getInstance(true));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (SpUtil.getInstance().getWalletPw() != null && isCanShow) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }

    }
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            RxToast.showToast(R.string.tips_exit);
        }
        return true;
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {
        UpdateResponseBean responseBean = new Gson().fromJson(response.getResponse(), UpdateResponseBean.class);
        if (responseBean!=null&&responseBean.getData()!=null){
            SpUtil.getInstance().setVersion(responseBean.getData().getVersion());
            SpUtil.getInstance().setUpdateUrl(responseBean.getData().getDownload_url());
        }
    }
}
