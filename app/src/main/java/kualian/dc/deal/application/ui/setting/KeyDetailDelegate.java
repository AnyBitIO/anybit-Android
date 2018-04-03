package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.MainNetParams;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.CoinIndex;

/**
 * Created by idmin on 2018/3/14.
 */

public class KeyDetailDelegate extends SourceDelegate {
    private TextView address, privateKey;
    private static final String TAG="tag";
    private String key;
    public static KeyDetailDelegate getInstance(String key){
        KeyDetailDelegate keyDetailDelegate=new KeyDetailDelegate();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,key);
        keyDetailDelegate.setArguments(bundle);
        return keyDetailDelegate;
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_key_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        key=getArguments().getString(TAG,"");
        findToolBar(rootView);
        address = rootView.findViewById(R.id.coin_address);
        privateKey = rootView.findViewById(R.id.coin_key);
        DeterministicKey deterministicKey = KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(key));
        String coinKey = KeyUtil.genSubPriKeyWifFromMasterKey(deterministicKey, CoinIndex.UBCOIN.index, MainNetParams.get());
        String coinAddress = KeyUtil.genSubPubAddrWifFromMasterKey(deterministicKey, CoinIndex.UBCOIN.index, MainNetParams.get());

        address.setText(coinAddress);
        privateKey.setText(coinKey);

        toolTitle.setText(R.string.ub_key);
        setOnClickViews(toolBack,privateKey,address);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.coin_key:
                CommonUtil.copyContent(_mActivity,privateKey.getText().toString());
                RxToast.showToast(R.string.tips_copy);
                break;
            case R.id.coin_address:
                CommonUtil.copyContent(_mActivity,address.getText().toString());
                RxToast.showToast(R.string.tips_copy);
                break;
            case R.id.toolbar_back:
                _mActivity.onBackPressed();
                break;
        }
    }
}
