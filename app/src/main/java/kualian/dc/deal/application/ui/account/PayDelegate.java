package kualian.dc.deal.application.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.RecordResponse;
import kualian.dc.deal.application.bean.ResponseCode;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.bean.ResultCode;
import kualian.dc.deal.application.bean.SendRequest;
import kualian.dc.deal.application.bean.TradeBean;
import kualian.dc.deal.application.bean.TradeResponse;
import kualian.dc.deal.application.callback.OnPwListener;
import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.presenter.home.HomeContract;
import kualian.dc.deal.application.presenter.impl.HomePresenterImpl;
import kualian.dc.deal.application.presenter.logic.HomeLogic;
import kualian.dc.deal.application.ui.camera.RequestCodes;
import kualian.dc.deal.application.ui.contact.ContactDelegate;
import kualian.dc.deal.application.ui.contact.ContactSelectDelegate;
import kualian.dc.deal.application.ui.scan.ActivityScanerCode;
import kualian.dc.deal.application.util.BitcoinAddressValidator;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.NetUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.util.window.SelectWindow;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.widget.codeView.PayWindow;

/**
 * Created by idmin on 2018/2/26.
 */

public class PayDelegate extends SourceDelegate implements OnPwListener {
    private static final String Sign = "PayDelegate";
    private TextView title, scan, back, address, coinName, coinMoney, cancel, goNext, minus, add, contact, moneyCoinKind, cashCoinkind;
    private AutoCompleteTextView payMoney, payAddress, payRemark, payCash;
    private String receive_address, key;
    private View view;
    private PayWindow window;
    private CoinType coinType;
    private ImageView icon, cashKind;
    private double minFee, maxFee;

    public static PayDelegate getInstance(CoinType coinType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Sign, coinType);
        PayDelegate receiveDelegate = new PayDelegate();
        receiveDelegate.setArguments(bundle);
        return receiveDelegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_pay;
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);

        if (getArguments() != null) {
            coinType = (CoinType) getArguments().getSerializable(Sign);
        }
        view = rootView;
        back = rootView.findViewById(R.id.toolbar_back);
        scan = rootView.findViewById(R.id.qr_code);
        title = rootView.findViewById(R.id.toolbar_title);

        address = rootView.findViewById(R.id.coin_address);
        coinName = rootView.findViewById(R.id.coin_name);
        coinMoney = rootView.findViewById(R.id.coin_money);
        icon = rootView.findViewById(R.id.coin_icon);

        cancel = rootView.findViewById(R.id.cancel);
        goNext = rootView.findViewById(R.id.next);
        minus = rootView.findViewById(R.id.minus);
        add = rootView.findViewById(R.id.add);
        contact = rootView.findViewById(R.id.contact);

        payMoney = rootView.findViewById(R.id.pay_money);
        payAddress = rootView.findViewById(R.id.pay_address);
        payRemark = rootView.findViewById(R.id.remark);
        payCash = rootView.findViewById(R.id.cash);

        //moneyKind = rootView.findViewById(R.id.select_kind);
        moneyCoinKind = rootView.findViewById(R.id.coin_kind);
        cashCoinkind = rootView.findViewById(R.id.cash_kind);
        rootView.findViewById(R.id.head).setBackgroundColor(getResources().getColor(R.color.transparent));

        minFee = Double.parseDouble(coinType.getMinFee());
        maxFee = Double.parseDouble(coinType.getMaxFee());
        payCash.setText(coinType.getRecommendFee());
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        setOnClickViews(scan, back, goNext, cancel, add, minus, contact);
        title.setText(R.string.wallet_pay);

        coinName.setText(coinType.getCoinName());
        address.setText(coinType.getCoinAddress());
        coinMoney.setText(coinType.getCoinNum());
        //icon.setImageResource(coinType.getCoinResource());
        moneyCoinKind.setText(coinType.getCoinName());
        cashCoinkind.setText(coinType.getCoinName());
    }

    public void buildTrade(String pw) {
        TradeBean requestBean = new TradeBean();
        TradeBean.HeaderBean headerBean = new TradeBean.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.tran_build);
        TradeBean.BodyBean body = new TradeBean.BodyBean(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(pw)), coinType.getCoinIndex(), MainNetParams.get()), coinType.getCoinName(), payAddress.getText().toString(), payMoney.getText().toString(), payRemark.getText().toString(), payCash.getText().toString());
        requestBean.setBody(body);
        requestBean.setHeader(headerBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.tran_build);
    }

    public void sendBroadcast(TradeResponse tradeResponse) {

        SendRequest requestBean = new SendRequest();
        SendRequest.HeaderBean headerBean = new SendRequest.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.tran_send);
        SendRequest.BodyBean body = new SendRequest.BodyBean(KeyUtil.genSignMessage(tradeResponse.getData().getInputs(),
                tradeResponse.getData().getUnsignTranHex(), coinType.getCoinAddress(),
                KeyUtil.genSubPriKeyWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(key)), coinType.getCoinIndex(), MainNetParams.get())));
        SendRequest.BodyBean.TradeBean tradeBean = new SendRequest.BodyBean.TradeBean(coinType.getCoinAddress(), coinType.getCoinName(), payAddress.getText().toString(), payMoney.getText().toString(), payRemark.getText().toString(), payCash.getText().toString());


        List<SendRequest.BodyBean.Input> inputList = new ArrayList<>();
        if (tradeResponse.getData().getInputs() == null) {
            return;
        }
        for (int i = 0; i < tradeResponse.getData().getInputs().size(); i++) {
            SendRequest.BodyBean.Input input = new SendRequest.BodyBean.Input(tradeResponse.getData().getInputs().get(i).getScriptPubKey()
                    , tradeResponse.getData().getInputs().get(i).getAmout()
                    , tradeResponse.getData().getInputs().get(i).getScriptPubKey()
                    , tradeResponse.getData().getInputs().get(i).getVout());
            inputList.add(input);
        }
        body.setInputs(inputList);
        body.setTranContent(tradeBean);
        requestBean.setBody(body);
        requestBean.setHeader(headerBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.tran_send);
    }

    double number;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.toolbar_back:
                CommonUtil.hideSoftInput(_mActivity);
                _mActivity.onBackPressed();
                break;
            case R.id.qr_code:
                startActivityForResult(new Intent(_mActivity, ActivityScanerCode.class), RequestCodes.SCAN);
                CallbackManager.getInstance()
                        .addCallback(CallbackType.ON_SCAN, new IGlobalCallback<String>() {
                            @Override
                            public void executeCallback(@Nullable String args) {
                                payAddress.setText(args);
                            }
                        });
                break;
            case R.id.next:
                LogUtils.i(payRemark.getText().toString());
                if (!CommonUtil.stringFilter(payRemark.getText().toString())) {
                    RxToast.showToast(R.string.tips_input_error);
                    return;
                }
               if (payRemark.getText().toString().length()>20){
                    RxToast.showToast(R.string.tips_input_max_long);
                    return;
                }
                if (!TextUtils.isEmpty(payAddress.getText().toString()) && !TextUtils.isEmpty(payMoney.getText().toString()) && !TextUtils.isEmpty(payCash.getText().toString())) {
                   /* String[] split = payCash.getText().toString().split("\\.");
                    if (split.length==2){
                        if (split[1].length()>8){
                            RxToast.showToast(R.string.tips_money_max);
                            return;
                        }
                    }*/
                   if (!BitcoinAddressValidator.assertBitcoin(payAddress.getText().toString().trim(), coinType.getCoinName()) && !CommonUtil.isNumeric(payAddress.getText().toString().trim())) {
                        RxToast.showToast(R.string.trade_address_error);
                        return;
                    }

                    if (Double.parseDouble(payCash.getText().toString()) < minFee) {
                        RxToast.showToast(R.string.tips_cash_lack);
                    } else if (Double.parseDouble(payCash.getText().toString()) > maxFee) {
                        RxToast.showToast(R.string.tips_fee_much);
                    } else {
                        window = new PayWindow(_mActivity, false, this);
                        window.show(view);
                    }
                } else {
                    RxToast.showToast(R.string.tips_finish);
                }
                break;
            case R.id.cancel:
                CommonUtil.hideSoftInput(_mActivity);
                _mActivity.onBackPressed();
                break;
            case R.id.contact:
                start(ContactSelectDelegate.getInstance(true));
                CallbackManager.getInstance().addCallback(CallbackType.ON_RESULT, new IGlobalCallback<String>() {
                    @Override
                    public void executeCallback(@Nullable String args) {
                        if (args != null) {
                            payAddress.setText(args);
                        }
                    }
                });
                break;
            case R.id.add:
                if (TextUtils.isEmpty(payCash.getText().toString())) {
                    number = 0;
                } else {
                    number = Double.parseDouble(payCash.getText().toString());
                }
                String add = add(number, minFee);
                if (Double.parseDouble(add) > maxFee) {
                    RxToast.showToast(R.string.tips_fee_much);
                    return;
                }
                payCash.setText(add(number, minFee));
                break;
            case R.id.minus:
                if (TextUtils.isEmpty(payCash.getText().toString())) {
                    RxToast.showToast(R.string.tips_cash_lack);
                } else if (Double.parseDouble(payCash.getText().toString()) <= minFee) {
                    RxToast.showToast(R.string.tips_cash_lack);
                } else {
                    number = Double.parseDouble(payCash.getText().toString());
                    if (number <= 0) {
                    } else {
                        payCash.setText(minus(number, minFee));
                    }
                }
            default:
        }
    }


    @Override
    public void getPw(String pw) {
        AppLoader.showLoading(_mActivity);
        if (KeyUtil.getPwMessage(pw).equals(SpUtil.getInstance().getWalletPw())) {
            key = pw;
            buildTrade(pw);
            window.dismiss();
        } else {
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.setTranslateAnimationX(window.getContainView());
                    WalletTool.vibrateOnce(_mActivity, 200);
                    window.getCodeView().clear();
                    AppLoader.stopLoading();
                    RxToast.showToast(getResources().getString(R.string.error_pw));
                }
            }, 200);
        }
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parseData(response, tag);
            }
        });

    }

    private void parseData(ResponseData response, String tag) {
        String result = response.getResponse();
        if (tag.equals(Constants.tran_build)) {
            TradeResponse tradeResponse = null;
            try {
                tradeResponse = new Gson().fromJson(result, TradeResponse.class);
            } catch (Exception e) {
                return;
            }
            if (tradeResponse != null) {
                if (tradeResponse.getRtnCode().equals(Constants.RTNCODE_OK)) {
                    if (KeyUtil.validateRawTran(coinType.getCoinAddress(), payAddress.getText().toString().trim(), new BigDecimal(payMoney.getText().toString().trim()), tradeResponse.getData().getUnsignTranHex())) {
                        sendBroadcast(tradeResponse);
                    } else {
                        RxToast.showToast(getResources().getString(R.string.view_no_network));
                    }
                } else {
                    if (NetUtil.isNetworkAvailable(getContext())) {
                        RxToast.showToast(tradeResponse.getErrMsg());
                    } else {
                        RxToast.showToast(getResources().getString(R.string.view_no_network));
                    }
                    AppLoader.stopLoading();
                }
            } else {
                AppLoader.stopLoading();
            }
        } else {
            AppLoader.stopLoading();
            ResponseCode resultCode = null;
            try {
                resultCode = new Gson().fromJson(result, ResponseCode.class);
            } catch (Exception e) {
                return;
            }
            if (resultCode.getRtnCode() == 1) {
                RxToast.showToast(R.string.trade_success);
                _mActivity.onBackPressed();
            } else {

            }


        }
    }

    public String add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal bigDecimal = new BigDecimal(b1.add(b2).doubleValue());
        return bigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String minus(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal bigDecimal = new BigDecimal(b1.subtract(b2).doubleValue());
        return bigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP).toString();
    }
}
