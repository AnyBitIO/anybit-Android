package kualian.dc.deal.application.ui.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.RecordResponse;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.web.WebDelegateImpl;

/**
 * Created by idmin on 2018/3/7.
 */

public class TradeSuccessDelegate extends SourceDelegate {
    private static final String TAG="TradeSuccessDelegate";
    private TextView mTradeTime, mTradeKind, mTradeAddress, mTradeMessage, mCoinNum, mTradeId,mTradeState;
    private ImageView mCoinIcon;
    private RecordResponse.DataBean.TradeBean tradeBean;
    public static TradeSuccessDelegate getInstance(RecordResponse.DataBean.TradeBean coinType){
        Bundle bundle=new Bundle();
        bundle.putSerializable(TAG,coinType);

        TradeSuccessDelegate trade=new TradeSuccessDelegate();
        trade.setArguments(bundle);
        return trade;

    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_trade_success;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        mTradeAddress = rootView.findViewById(R.id.trade_address);
        mTradeTime = rootView.findViewById(R.id.trade_time);
        mTradeKind = rootView.findViewById(R.id.trade_kind);
        mTradeMessage = rootView.findViewById(R.id.trade_remark);
        mTradeId = rootView.findViewById(R.id.trade_id);
        mTradeState = rootView.findViewById(R.id.trade_state);
        mCoinNum = rootView.findViewById(R.id.coin_num);
        mCoinIcon = rootView.findViewById(R.id.coin_icon);
        findToolBar(rootView);
        setOnClickViews(toolBack,mTradeId,mTradeAddress);
        tradeBean= (RecordResponse.DataBean.TradeBean) getArguments().getSerializable(TAG);
        toolNext.setVisibility(View.GONE);
        toolTitle.setVisibility(View.VISIBLE);
        toolTitle.setText(R.string.record_detail);
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        mTradeTime.setText(CommonUtil.stampToDate(tradeBean.getTranTime()));
        mTradeMessage.setText(tradeBean.getBak());
        mTradeAddress.setText(tradeBean.getTargetAddr());
        mCoinNum.setText(tradeBean.getTranAmt()+" UBTC");
        mTradeId.setText(tradeBean.getTxId());
        if (tradeBean.getTranType().equals("1")){
            mTradeKind.setText(R.string.trade_pay);
        }else {
            mTradeKind.setText(R.string.trade_receive);
        }
        if (tradeBean.getTranState().equals("1")) {
            mTradeState.setText(R.string.record_unsure);
            mTradeState.setTextColor(getResources().getColor(R.color.text_red));
        } else if (tradeBean.getTranState().equals("2")) {
            mTradeState.setText(R.string.record_success);
            mTradeState.setTextColor(getResources().getColor(R.color.green));
        } else {
            mTradeState.setText(R.string.record_invalid);
            mTradeState.setTextColor(getResources().getColor(R.color.text_red));
        }
       // mTradeTime.setText(tradeBean.getTranTime());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.toolbar_back){
            _mActivity.onBackPressed();
        }
        else if (view.getId()==R.id.trade_address){
            CommonUtil.copyContent(_mActivity, mTradeAddress.getText().toString());
            RxToast.showToast(R.string.trade_id);
        }else {
            start(WebDelegateImpl.getInstance("https://block.bitbank.com/tx/ubtc/"+mTradeId.getText().toString()));
            //start(WebDelegateImpl.getInstance("https://www.baidu.com"));
        }
    }
}
