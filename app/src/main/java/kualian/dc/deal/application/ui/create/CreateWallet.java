package kualian.dc.deal.application.ui.create;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.MainNetParams;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
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
import kualian.dc.deal.application.ui.MainDelegate;
import kualian.dc.deal.application.ui.adapter.CoinAdapter;
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
import kualian.dc.deal.application.wallet.coins.TVMain;
import kualian.dc.deal.application.wallet.coins.UbMain;
import kualian.dc.deal.application.widget.DivItemDecoration;
import kualian.dc.deal.application.widget.codeView.PayWindow;

/**
 * Created by idmin on 2018/2/24.
 */

public class CreateWallet extends BaseListDelegate implements OnCoinSelectListener,AddCoinTask.Listener{
    private List<CoinType> mData = new ArrayList<>();
    private List<CoinType> mSelect;
    //是添加货币还是第一次创建货币
    IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.COIN_TAG);
    IGlobalCallback webback = CallbackManager.getInstance().getCallback(CallbackType.WEB_TAG);
    private CoinDao coinDao = new CoinDao();
    private PayWindow payWindow;
    private static final String KEY = "key";
    private String mKey;

    public static CreateWallet getInstance(boolean isAdd, String key) {
        CreateWallet addCoin = new CreateWallet();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TAG, isAdd);
        bundle.putString(KEY, key);
        addCoin.setArguments(bundle);
        return addCoin;
    }


    @Override
    protected void onEvent() {
        super.onEvent();
        AppLoader.showLoading(getContext());
        AppLoader.setMessage(getResources().getString(R.string.coin_load));
        viewStub.setLayoutResource(R.layout.button_create);
        View inflate = viewStub.inflate();
        inflate.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelect != null && mSelect.size() > 0) {
                    refreshCoin();
                } else {
                    RxToast.showToast(getResources().getString(R.string.account_select_tips));
                }

            }
        });
        SpUtil.getInstance().setIsRestore(true);
        mHead.setVisibility(View.GONE);
        //mNext.setTextColor(getResources().getColor(R.color.white));
        mBack.setVisibility(View.GONE);
        mNext.setText(R.string.button_skip);
        mNext.setTextColor(getResources().getColor(R.color.gray_light_));
        mTitle.setText(R.string.coin_add);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
        mNext.setVisibility(View.VISIBLE);
        mNext.setCompoundDrawables(null, null, null, null);
        setOnClickViews(mNext);
        if (getArguments() != null) {
            mKey = getArguments().getString(KEY);
        }
        AddCoinTask addCoinTask=new AddCoinTask(this,coinDao,mKey);
        addCoinTask.execute();
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
        if (callback != null) {
            callback.executeCallback(true);
            webback.executeCallback(true);
        }
        popTo(MainDelegate.class, false);

    }

    //刷新钱包
    private void refreshCoin() {
        if (mSelect == null || mSelect.size() == 0) {
            return;
        }
        AppLoader.showLoading(getContext());
        AppLoader.setMessage(getResources().getString(R.string.account_create));
        createWallet(mSelect);
    }


    private void createWallet(List<CoinType> coins) {
        HomeBean requestBean = new HomeBean();
        HomeBean.HeaderBean headerBean = new HomeBean.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.wallet_create);
        List<HomeBean.BodyBean.AddrsBean> list = new ArrayList<>();
        for (CoinType coin : coins) {
            HomeBean.BodyBean.AddrsBean address = new HomeBean.BodyBean.AddrsBean(coin.getCoinAddress(), coin.getCoinName());
            address.setCoinAddr(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(mKey)), coin.getCoinIndex(), MainNetParams.get()));
            list.add(address);
        }
        /*HomeBean.BodyBean.AddrsBean address = new HomeBean.BodyBean.AddrsBean("100", "100");
        list.add(address);*/
        HomeBean.BodyBean bodyBean = new HomeBean.BodyBean(list);
        requestBean.setBody(bodyBean);
        requestBean.setHeader(headerBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.wallet_create);
    }

    @Override
    public void onCoinSelect(List<CoinType> list) {
        mSelect = list;
    }

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
                        for (CoinType coin : mSelect) {
                            //coin.setCoinAddress(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(mKey)), coin.getCoinIndex(), MainNetParams.get()));
                            coinDao.update(coin.getCoinName(), Constants.IS_ADD,coin.getCoinAddress());
                        }
                        if (callback != null) {
                            callback.executeCallback(true);
                            webback.executeCallback(true);
                        }
                        popTo(MainDelegate.class, false);

                    } else {
                        RxToast.showToast(R.string.view_network_error);
                    }
                }
            }
        });

    }

    @Override
    public void onAddCoinTaskFinished() {
        smartRefreshLayout.setRefreshing(false);
        mData.addAll(coinDao.queryAll(SpUtil.getInstance().getWalletID()));
        mAdapter = new CoinAdapter(R.layout.delegate_add_coin, mData, this, false, coinDao);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DivItemDecoration(getContext(), false));
        mAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.addItemDecoration(new DivItemDecoration(getContext(), false));
        AppLoader.stopLoading();
    }
}
