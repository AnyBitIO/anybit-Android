package kualian.dc.deal.application.ui.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.RequestBean;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.bean.Test;
import kualian.dc.deal.application.bean.UpdateRequestBean;
import kualian.dc.deal.application.bean.UpdateResponseBean;
import kualian.dc.deal.application.bean.WalletRequest;
import kualian.dc.deal.application.bean.WalletResponse;
import kualian.dc.deal.application.database.AssetDao;
import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.ui.adapter.WalletCoinAdapter;
import kualian.dc.deal.application.ui.create.AddCoin;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.HttpUtil;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.UpdateUtil;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.widget.DivItemDecoration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by idmin on 2018/2/10.
 */

public class WalletDelegate extends SourceDelegate {
    private RecyclerView mRecyclerView;
    private TextView mAllMoney, mSymbol, mAdd, empty_add, mSelect;
    private ImageView mPay, mReceive;
    private static WalletDelegate instance = null;
    private BaseQuickAdapter mAdapter;
    private List<CoinType> mList = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private List<String> mCoins = new ArrayList<>();
    private LinearLayoutCompat empty, content;
    private SwitchCompat mSwitch;
    private String totalUsdtAmt, totalCnyAmt, defaultLanguage;
    private CoinDao coinDao;
    private AssetDao assetDao;
    private String walletId;

    public static WalletDelegate getInstance() {
        return new WalletDelegate();
    }

    @Override
    protected void onEvent() {
        walletId = SpUtil.getInstance().getWalletID();
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        if (!TextUtils.isEmpty(walletId)) {
            coinDao = new CoinDao();
            assetDao = new AssetDao();
            mList = coinDao.querySelectAll(Constants.IS_ADD);
        }
        if (mList != null && mList.size() > 0) {
            mCoins.clear();
            mList.get(0).setCoinIndex(2);
            for (int i = 0; i < mList.size(); i++) {
                mCoins.add(mList.get(i).getCoinName());
            }
        } else {
            empty.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new WalletCoinAdapter(R.layout.item_coin_list_row, mList, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.addItemDecoration(new DivItemDecoration(getContext(), false));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CoinType coinType = (CoinType) adapter.getItem(position);
                getParentDelegate().start(RecordDelegate.getInstance(coinType));
            }
        });
        if (!TextUtils.isEmpty(walletId)) {
            setData(new Gson().fromJson(assetDao.queryAsset(walletId), WalletResponse.class));
        }
        callNeT();
        //checkUpdateNet();
        refreshWallet();
        checkUpdate();

        //switchAccount();
    }

    private void switchAccount() {
        walletId = SpUtil.getInstance().getWalletID();
        coinDao = new CoinDao();
        assetDao = new AssetDao();
    }


    private void checkUpdate() {
        if (!SpUtil.getInstance().getCheckDate().equals(CommonUtil.getCurrentDate())) {
            if (!SpUtil.getInstance().getDefaultVersion().equals(SpUtil.getInstance().getVersion())) {
                if (getContext().getExternalFilesDir("") != null) {
                    UpdateUtil.check(getContext(), false, true, false, false, false, 998);
                }
                SpUtil.getInstance().setCheckDate(CommonUtil.getCurrentDate());
            }
        }
    }

    private void callNeT() {
        if (!TextUtils.isEmpty(walletId)) {
            WalletRequest.HeaderBean header = new WalletRequest.HeaderBean(KeyUtil.getRandom());
            header.setTrancode(Constants.asset_query);
            WalletRequest.BodyBean body = new WalletRequest.BodyBean(mCoins);
            WalletRequest contactHandle = new WalletRequest();
            contactHandle.setBody(body);
            contactHandle.setHeader(header);
            presenter.queryServiceData(new Gson().toJson(contactHandle), Constants.asset_query);
        }
    }

    private void refreshWallet() {
        CallbackManager.getInstance().addCallback(CallbackType.COIN_TAG, new IGlobalCallback<Boolean>() {
            @Override
            public void executeCallback(@Nullable Boolean args) {
                switchAccount();
                mList = coinDao.querySelectAll(Constants.IS_ADD);
                if (mList != null) {
                    mCoins.clear();
                    if (mList.size() == 0) {
                        empty.setVisibility(View.VISIBLE);
                        content.setVisibility(View.GONE);
                    } else {
                        mList.get(0).setCoinIndex(2);
                        empty.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < mList.size(); i++) {
                        mCoins.add(mList.get(i).getCoinName());
                    }
                    mAdapter.getData().clear();
                    mAdapter.addData(mList);
                    callNeT();
                } else {
                    empty.setVisibility(View.VISIBLE);
                    content.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_account;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recycle_coin);
        refreshLayout = rootView.findViewById(R.id.refresh_layout);
        mAllMoney = rootView.findViewById(R.id.coin_total_money);
        mSymbol = rootView.findViewById(R.id.coin_total_symbol);
        mAdd = rootView.findViewById(R.id.coin_add);
        mPay = rootView.findViewById(R.id.coin_pay);
        mReceive = rootView.findViewById(R.id.coin_receive);
        empty_add = rootView.findViewById(R.id.empty_add);
        mSelect = rootView.findViewById(R.id.select);
        empty = rootView.findViewById(R.id.empty_view);
        content = rootView.findViewById(R.id.content);
        mSwitch = rootView.findViewById(R.id.money_select);
        defaultLanguage = SpUtil.getInstance().getDefaultLanguage();
        if (defaultLanguage == null) {
            defaultLanguage = Locale.getDefault().getLanguage();
            if (!defaultLanguage.equals(Constants.LANGAE_ZH)) {
                defaultLanguage = Constants.LANGAE_EN;
            }
        }
        SpUtil.getInstance().setDefaultMoney(defaultLanguage.equals(Constants.LANGAE_EN));
        if (defaultLanguage.equals(Constants.LANGAE_EN)) {
            mPay.setImageResource(R.drawable.pay_en);
            mReceive.setImageResource(R.drawable.receive_en);
        }

        mSwitch.setChecked(SpUtil.getInstance().getDefaultMoney());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpUtil.getInstance().setDefaultMoney(b);
                if (b) {
                    mAllMoney.setText(totalUsdtAmt);
                    mSymbol.setText("$");
                } else {
                    mAllMoney.setText(totalCnyAmt);
                    mSymbol.setText("¥");
                }
                mAdapter.notifyItemChanged(0);
            }
        });
        setOnClickViews(mAdd, mPay, mReceive, empty_add);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callNeT();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.coin_add:
                getParentDelegate().start(AddCoin.getInstance(true, null));
                break;
            case R.id.empty_add:
                getParentDelegate().start(AddCoin.getInstance(true, null));
                break;
            case R.id.coin_pay:
                getParentDelegate().start(PayDelegate.getInstance(mList.get(0)));
                break;

            case R.id.coin_receive:
                getParentDelegate().start(ReceiveDelegate.getInstance(mList.get(0)));
                break;
            default:
        }
    }

    public void setData(WalletResponse walletResponse) {
        if (walletResponse == null) {
            return;
        }
        if (walletResponse.getData().getAssets() != null) {
            totalCnyAmt = walletResponse.getData().getTotalCnyAmt();
            totalUsdtAmt = walletResponse.getData().getTotalUsdtAmt();
            if (SpUtil.getInstance().getDefaultMoney()) {
                mAllMoney.setText(totalUsdtAmt);
                mSymbol.setText("$");
            } else {
                mAllMoney.setText(totalCnyAmt);
                mSymbol.setText("¥");
            }
            List<WalletResponse.DataBean.AssetsBean> assets = walletResponse.getData().getAssets();
            if (assets.size() > 0 && mList.size() > 0) {
                mList.get(0).setCoinNum(assets.get(0).getNum());
                mList.get(0).setCoinMoney(assets.get(0).getCnyAmt());
                mList.get(0).setUsMoney(assets.get(0).getUsdtAmt());
                mList.get(0).setMinFee(assets.get(0).getMinFee());
                mList.get(0).setMaxFee(assets.get(0).getMaxFee());
                mList.get(0).setRecommendFee(assets.get(0).getRecommendFee());
            }
            mAdapter.notifyItemChanged(0);
        }
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                if (tag.equals(Constants.asset_query)) {
                    if (response.getResponse() != null) {
                        WalletResponse walletResponse = null;
                        try {
                            walletResponse = new Gson().fromJson(response.getResponse(), WalletResponse.class);
                        } catch (Exception e) {
                            RxToast.showToast(R.string.error_state);
                            return;
                        }
                        if (walletResponse != null) {
                            if (walletResponse.getErrCode().equals(Constants.ERRCODE_ERROR)) {
                                if (assetDao != null && assetDao.queryAsset(walletId) != null) {
                                    walletResponse = new Gson().fromJson(assetDao.queryAsset(walletId), WalletResponse.class);
                                }
                            } else {
                                if (assetDao != null) {
                                    assetDao.deleteWithId(walletId);
                                    assetDao.add(response.getResponse(), walletId);
                                }
//                                LogUtils.i("asset"+assetDao.queryAsset());
                            }
                        }
                        if (walletResponse.getData() == null) {
                            return;
                        }
                        setData(walletResponse);
                    }
                }
            }
        });

    }


}
