package kualian.dc.deal.application.ui.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import org.bitcoinj.params.TestNet3Params;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseListDelegate;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.bean.RecordQuery;
import kualian.dc.deal.application.bean.RecordResponse;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.ui.adapter.RecordAdapter;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by idmin on 2018/2/28.
 */

public class RecordDelegate extends BaseListDelegate {
    private static final String TAG = "RecordDelegate";
    private List<RecordResponse.DataBean.TradeBean> list = new ArrayList<>();
    private CoinType coinType;
    private View mHeadView;
    private TextView iconMoney;
    private boolean isFirst, canRefresh;

    public static RecordDelegate getInstance(CoinType coinType) {
        RecordDelegate recordDelegate = new RecordDelegate();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, coinType);
        recordDelegate.setArguments(bundle);
        return recordDelegate;
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        if (getArguments() != null) {
            coinType = (CoinType) getArguments().getSerializable(TAG);
        }
        isFirst = true;
        smartRefreshLayout.setRefreshing(true);
        viewStub.setLayoutResource(R.layout.view_stub_foot);
        View inflate = viewStub.inflate();
        LinearLayoutCompat pay = inflate.findViewById(R.id.pay);
        LinearLayoutCompat receive = inflate.findViewById(R.id.receive);
        setOnClickViews(pay, receive);
        mAdapter = new RecordAdapter(R.layout.item_record, list, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mTitle.setText(getString(R.string.record_name));

        //绑定空视图
        bindEmptyView();
        mAdapter.getEmptyView().setVisibility(View.GONE);
        empty.setText(R.string.record_no);
        mHeadView = LayoutInflater.from(getContext()).inflate(R.layout.include_address, (ViewGroup) mRecyclerView.getParent(), false);
        //绑定头部视图
        ImageView icon = mHeadView.findViewById(R.id.coin_icon);
        TextView iconName = mHeadView.findViewById(R.id.coin_name);
        iconMoney = mHeadView.findViewById(R.id.coin_money);
        TextView iconAddress = mHeadView.findViewById(R.id.coin_address);
        if (coinType != null) {
            icon.setImageResource(R.drawable.coin_ubtc);
            iconName.setText(coinType.getCoinName());
            iconAddress.setText(coinType.getCoinAddress());
            iconMoney.setText(coinType.getCoinNum());
        }
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                start(TradeSuccessDelegate.getInstance(list.get(position)));
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
        Latte.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                queryRecords();
            }
        },200);
    }

    private void queryRecords() {
        RecordQuery requestBean = new RecordQuery();
        RecordQuery.HeaderBean headerBean = new RecordQuery.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.tran_query);
        RecordQuery.BodyBean body = new RecordQuery.BodyBean(coinType.getCoinAddress(), coinType.getCoinName());
        requestBean.setBody(body);
        requestBean.setHeader(headerBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.tran_query);
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        queryRecords();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.pay:
                start(PayDelegate.getInstance(coinType));
                break;
            case R.id.receive:
                start(ReceiveDelegate.getInstance(coinType));
                break;
        }
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.getEmptyView().setVisibility(View.VISIBLE);
                smartRefreshLayout.setRefreshing(false);
                if (response.getResponse() != null) {
                    RecordResponse recordResponse = null;
                    try {
                        recordResponse = new Gson().fromJson(response.getResponse(), RecordResponse.class);
                    } catch (Exception e) {
                    }
                    if (recordResponse != null) {
                        if (recordResponse.getErrCode().equals(Constants.ERRCODE_ERROR)) {
                            empty.setText(R.string.view_network_error);
                        }
                    }

                    if (recordResponse != null && recordResponse.getData() != null) {
                        iconMoney.setText(recordResponse.getData().getNum());
                        if (recordResponse.getData().getTrans() != null) {
                            if (isFirst) {
                                mAdapter.addHeaderView(mHeadView);
                            }
                            isFirst = false;
                            list = recordResponse.getData().getTrans();
                            mAdapter.getData().clear();
                            mAdapter.addData(list);
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            CommonUtil.switchLanguage(SpUtil.getInstance().getDefaultLanguage());
            queryRecords();
        }
    }
}
