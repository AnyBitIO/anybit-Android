package kualian.dc.deal.application.ui.create;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.gson.Gson;

import org.bitcoinj.params.MainNetParams;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.BaseListDelegate;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.bean.HomeBean;
import kualian.dc.deal.application.bean.ResponseCode;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.callback.OnCoinSelectListener;
import kualian.dc.deal.application.callback.OnPwListener;
import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.presenter.home.HomeContract;
import kualian.dc.deal.application.presenter.impl.HomePresenterImpl;
import kualian.dc.deal.application.presenter.logic.HomeLogic;
import kualian.dc.deal.application.ui.MainDelegate;
import kualian.dc.deal.application.ui.adapter.AddCoinAdapter;
import kualian.dc.deal.application.ui.adapter.CoinAdapter;
import kualian.dc.deal.application.ui.setting.KeyDetailDelegate;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.wallet.coins.BitcoinMain;
import kualian.dc.deal.application.wallet.coins.TVMain;
import kualian.dc.deal.application.wallet.coins.UbMain;
import kualian.dc.deal.application.widget.DivItemDecoration;
import kualian.dc.deal.application.widget.codeView.PayWindow;

/**
 * Created by idmin on 2018/2/24.
 */

public class AddCoin extends BaseListDelegate implements OnPwListener {
    private List<CoinType> mData = new ArrayList<>();
    private List<CoinType> mSelect = new ArrayList<>();
    //是添加货币还是第一次创建货币
    IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.COIN_TAG);
    private boolean isAdd;
    private CoinDao coinDao = new CoinDao();
    private PayWindow payWindow;
    private static final String KEY = "key";
    private String mKey;

    public static AddCoin getInstance(boolean isAdd, String key) {
        AddCoin addCoin = new AddCoin();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TAG, isAdd);
        bundle.putString(KEY, key);
        addCoin.setArguments(bundle);
        return addCoin;
    }


    @Override
    protected void onEvent() {
        super.onEvent();
        smartRefreshLayout.setRefreshing(false);
        if (getArguments() != null) {
            isAdd = getArguments().getBoolean(TAG);
            mKey = getArguments().getString(KEY);
        }
        mData = coinDao.queryAll(SpUtil.getInstance().getWalletID());
        mNext.setText(R.string.sure);
        payWindow = new PayWindow(_mActivity, false, this);
        mAdapter = new AddCoinAdapter(R.layout.delegate_add_coin, mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DivItemDecoration(getContext(), false));
        mAdapter.bindToRecyclerView(mRecyclerView);
        mTitle.setText(R.string.coin_add);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
        mNext.setVisibility(View.VISIBLE);
        mNext.setCompoundDrawables(null, null, null, null);
        mRecyclerView.addItemDecoration(new DivItemDecoration(getContext(), false));
        setOnClickViews(mNext);
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        smartRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (isAdd) {
            payWindow.show(view);
        } else {
            popTo(MainDelegate.class, false);
        }
    }

    //刷新钱包
    private void refreshCoin() {
        AppLoader.showLoading(getContext());
        AppLoader.setMessage(getResources().getString(R.string.coin_add));
        for (CoinType coin : mData) {
            if (coin.getAddTag().equals(Constants.IS_ADD)) {
                mSelect.add(coin);
            }
        }
        if (mSelect.size() > 0) {
            addCoins(mSelect);
        } else {
            AppLoader.stopLoading();
            updateCoinDao(mData);
            _mActivity.onBackPressed();
        }
    }

    //向服务器请求添加货币
    private void addCoins(List<CoinType> coins) {
        if (coins == null) {
            return;
        }
        HomeBean requestBean = new HomeBean();
        HomeBean.HeaderBean headerBean = new HomeBean.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.coin_add);
        List<HomeBean.BodyBean.AddrsBean> list = new ArrayList<>();
        for (CoinType coin : coins) {
            HomeBean.BodyBean.AddrsBean address = new HomeBean.BodyBean.AddrsBean(coin.getCoinAddress(), coin.getCoinName());
            address.setCoinAddr(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(mKey)), coin.getCoinIndex(), MainNetParams.get()));
            list.add(address);
        }

        HomeBean.BodyBean bodyBean = new HomeBean.BodyBean(list);
        requestBean.setBody(bodyBean);
        requestBean.setHeader(headerBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.coin_add);
    }

  /*  private void createWallet(List<CoinType> coins) {
        HomeBean requestBean = new HomeBean();
        HomeBean.HeaderBean headerBean = new HomeBean.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.wallet_create);
        List<HomeBean.BodyBean.AddrsBean> list = new ArrayList<>();
        for (CoinType coin : coins) {
            HomeBean.BodyBean.AddrsBean address = new HomeBean.BodyBean.AddrsBean(coin.getCoinAddress(), coin.getCoinName());
            address.setCoinAddr(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(mKey)), coin.getCoinIndex(), MainNetParams.get()));
            list.add(address);
        }
        *//*HomeBean.BodyBean.AddrsBean address = new HomeBean.BodyBean.AddrsBean("100", "100");
        list.add(address);*//*
        HomeBean.BodyBean bodyBean = new HomeBean.BodyBean(list);
        requestBean.setBody(bodyBean);
        requestBean.setHeader(headerBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.wallet_create);
    }*/


    @Override
    public void getServiceData(ResponseData response, String tag) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AppLoader.stopLoading();
                if (response.getResponse() != null) {
                    ResponseCode responseCode = null;
                    try {
                        responseCode = new Gson().fromJson(response.getResponse(), ResponseCode.class);
                    } catch (Exception e) {
                        return;
                    }
                    if (responseCode != null && responseCode.getRtnCode() == 1) {
                        updateCoinDao(mData);
                        if (!isAdd) {
                            popTo(MainDelegate.class, false);
                        } else {
                            _mActivity.onBackPressed();
                        }
                    } else {
                        RxToast.showToast(R.string.view_network_error);
                    }
                }
            }
        });

    }

    //更新数据库
    private void updateCoinDao(List<CoinType> mData) {
        for (CoinType coin : mData) {
            coin.setCoinAddress(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(mKey)), coin.getCoinIndex(), MainNetParams.get()));
            coinDao.update(coin.getCoinName(), coin.getAddTag(), coin.getCoinAddress());
        }
        if (callback != null) {
            callback.executeCallback(false);
        }
    }

    @Override
    public void getPw(String pw) {
        if (KeyUtil.getPwMessage(pw).equals(SpUtil.getInstance().getWalletPw())) {
            payWindow.getCodeView().clear();
            payWindow.dismiss();
            mKey = pw;
            refreshCoin();
            //_mActivity.onBackPressed();
        } else {
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.setTranslateAnimationX(payWindow.getContainView());
                    WalletTool.vibrateOnce(_mActivity, 200);
                    payWindow.getCodeView().clear();
                    // AppLoader.stopLoading();
                    RxToast.showToast(getResources().getString(R.string.error_pw));
                }
            }, 200);
        }
    }
}
