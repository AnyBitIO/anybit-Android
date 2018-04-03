package kualian.dc.deal.application.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceActivity;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.callback.OnPwListener;
import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.database.AssetDao;
import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.database.ContactDao;
import kualian.dc.deal.application.database.DataBaseUtil;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.database.WalletTable;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.util.AesUtil;
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
import kualian.dc.deal.application.util.dialog.RxDialogSureCancel;
import kualian.dc.deal.application.wallet.WalletAccount;
import kualian.dc.deal.application.widget.codeView.PayWindow;

/**
 * Created by idmin on 2018/3/21.
 */

public class PersonalCenter extends SourceDelegate implements OnPwListener{
    private TextView mKey;
    private Button btn;
    private AutoCompleteTextView mNick;
    private static final String TAG="tag";
    private WalletDao walletDao=new WalletDao();
    private RxDialogSureCancel dialogSureCancel;
    private PayWindow payWindow;
    public static PersonalCenter getInstance(String nick){
        PersonalCenter personalCenter =new PersonalCenter();
        Bundle bundle =new Bundle();
        bundle.putString(TAG,nick);
        personalCenter.setArguments(bundle);
        return personalCenter;
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        btn = rootView.findViewById(R.id.delete_wallet);
        mKey = rootView.findViewById(R.id.setting_key);
        mNick = rootView.findViewById(R.id.setting_nick);
        String string = getArguments().getString(TAG);
        mNick.setText(string);
        findToolBar(rootView);
        toolTitle.setText(R.string.setting_personal);
        toolNext.setText(R.string.sure);
        dialogSureCancel=new RxDialogSureCancel(getContext());
        payWindow=new PayWindow(getContext(),false,this);
        setOnClickViews(mKey,toolBack,btn,toolNext);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.setting_key:
                start(new PrivateKeyDelegate());
                break;
            case R.id.toolbar_back:
                CommonUtil.hideSoftInput(_mActivity);
                _mActivity.onBackPressed();
                break;
            case R.id.delete_wallet:
                dialogSureCancel.getTitleView().setText(R.string.tips_watch);
                dialogSureCancel.getTitleView().setTextColor(getResources().getColor(R.color.text_red));
                dialogSureCancel.getContentView().setText(R.string.tips_delete_content);
                dialogSureCancel.getCancelView().setText(getResources().getString(R.string.delete));
                dialogSureCancel.getSureView().setText(getResources().getString(R.string.cancel));
                dialogSureCancel.getSureView().setTextColor(getResources().getColor(R.color._9));

                dialogSureCancel.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogSureCancel.dismiss();
                        payWindow.show(view);
                    }
                });
                dialogSureCancel.setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogSureCancel.cancel();
                    }
                });
                dialogSureCancel.show();
                break;
            case R.id.toolbar_next:
                String name = mNick.getText().toString().trim();
                if (TextUtils.isEmpty(name)||!CommonUtil.stringFilter(name)){
                    RxToast.showToast(R.string.tips_input_error);
                }else if (name.length()>10){
                    RxToast.showToast(R.string.tips_input_long);

                }
                else {
                    List<WalletAccount> walletAccounts = walletDao.queryAll();
                    if (walletAccounts.size()>0){
                        for (WalletAccount account : walletAccounts) {
                            if (account.getWalletName().equals(name)) {
                                RxToast.showToast(R.string.tips_name_repetition);
                                return;
                            }
                        }
                    }
                    walletDao.update(WalletTable.WALLET_NAME,name, SpUtil.getInstance().getWalletID());
                    SpUtil.getInstance().setWalletName(name);

                    IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.ON_NAME_CHANGE);
                    if (callback!=null){
                        callback.executeCallback(null);
                    }
                    CommonUtil.hideSoftInput(_mActivity);
                    _mActivity.onBackPressed();
                }
                break;
        }
    }

    @Override
    public void getPw(String pw) {
        if (KeyUtil.getPwMessage(pw).equals(SpUtil.getInstance().getWalletPw())) {
            payWindow.dismiss();
            AppLoader.showLoading(getContext());
            AppLoader.setMessage(getResources().getString(R.string.setting_delete_ing));
            String walletID = SpUtil.getInstance().getWalletID();

            Latte.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    DataBaseUtil.deleteDataWithId(walletID);
                    SpUtil.getInstance().deleteData();
                    Intent intent = new Intent(_mActivity, SourceActivity.class);
                    startActivity(intent);
                    AppLoader.stopLoading();
                    _mActivity.finish();
                }
            });
        } else {
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.setTranslateAnimationX(payWindow.getContainView());
                    WalletTool.vibrateOnce(_mActivity, 200);
                    payWindow.getCodeView().clear();
                    AppLoader.stopLoading();
                    RxToast.showToast(getResources().getString(R.string.error_pw));
                }
            }, 200);
        }
    }
}
