package kualian.dc.deal.application.ui.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.bitcoinj.params.TestNet3Params;

import java.lang.ref.WeakReference;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.RxQRCode;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by idmin on 2018/2/26.
 */

public class ReceiveDelegate extends SourceDelegate {
    private static final String Sign = "sign";
    private static final int code = 1;
    private TextView title, next, back, address,coinAddress, coinName, coinMoney, copy;
    private static ImageView qr;
    private ImageView icon;
    private View divide;
    private String receive_address;
    private Handler mHandler;
    public static ReceiveDelegate getInstance(CoinType coinType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Sign, coinType);
        ReceiveDelegate receiveDelegate = new ReceiveDelegate();
        receiveDelegate.setArguments(bundle);
        return receiveDelegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_receive;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        back = rootView.findViewById(R.id.toolbar_back);
        next = rootView.findViewById(R.id.toolbar_next);
        title = rootView.findViewById(R.id.toolbar_title);

        address = rootView.findViewById(R.id.coin_address);
        coinAddress = rootView.findViewById(R.id.address);
        coinName = rootView.findViewById(R.id.coin_name);
        coinMoney = rootView.findViewById(R.id.coin_money);
        copy = rootView.findViewById(R.id.copy_address);
        qr = rootView.findViewById(R.id.qr_code);
        icon = rootView.findViewById(R.id.coin_icon);
        divide = rootView.findViewById(R.id.divider);

        divide.setVisibility(View.GONE);
        address.setVisibility(View.GONE);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_share);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        next.setCompoundDrawables(drawable, null, null, null);
        next.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.head).setBackgroundColor(getResources().getColor(R.color.transparent));
        mHandler=new MyHandler(this);
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        setOnClickViews(next, back, copy);
        Bundle bundle = getArguments();
        CoinType coinType = (CoinType) bundle.getSerializable(Sign);
        title.setText(R.string.wallet_receipt);
        if (coinType != null) {
            receive_address = coinType.getCoinAddress();
            coinAddress.setText(receive_address);
            coinName.setText(coinType.getCoinName());
            coinMoney.setText(coinType.getCoinNum());
            //icon.setImageResource(coinType.getCoinResource());
            //QrUtils.setQr(qr,getResources(),receive_address);
            //二维码生成方式
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap srBitmap = RxQRCode.builder(receive_address).
                            backColor(getResources().getColor(R.color.white)).
                            codeColor(getResources().getColor(R.color.black)).
                            codeSide(600).
                            getSrBitmap();
                    Message message = mHandler.obtainMessage();
                    message.obj=srBitmap;
                    message.what=code;
                    mHandler.sendMessage(message);
                }
            }).start();

        }
    }
    protected static class MyHandler extends Handler {
        private WeakReference<ReceiveDelegate> delegate;

        public MyHandler(ReceiveDelegate object) {
            delegate = new WeakReference<ReceiveDelegate>(object);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ReceiveDelegate activity = delegate.get();
            if (activity == null)
                return;
            if (msg.what == code) {
                Bitmap bitmap = (Bitmap) msg.obj;
                qr.setImageBitmap(bitmap);

                //处理逻辑
            }
        }
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.toolbar_back:
                _mActivity.onBackPressed();
                break;
            case R.id.toolbar_next:
               /* startActivityForResult(new Intent(_mActivity,ScannerDelegate.class), RequestCodes.SCAN);
                CallbackManager.getInstance()
                        .addCallback(CallbackType.ON_SCAN, new IGlobalCallback<String>() {
                            @Override
                            public void executeCallback(@Nullable String args) {
                                mnemonicTextView.setText(args);
                            }
                        });*/

                Intent shareIntent = new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, receive_address);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
                break;
            case R.id.copy_address:
                RxToast.showToast(R.string.copy_address_ok);
                CommonUtil.copyContent(_mActivity, receive_address);
                break;
           /* case R.id.copy_address:
                RxToast.showToast(R.string.copy_address_ok);
                CommonUtil.copyContent(_mActivity, receive_address);
                break;*/
            default:
        }
    }
}
