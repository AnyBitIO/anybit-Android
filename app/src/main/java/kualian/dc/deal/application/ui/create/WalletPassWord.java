package kualian.dc.deal.application.ui.create;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.callback.OnCloseDelegateListener;
import kualian.dc.deal.application.database.DataBaseUtil;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.util.AesUtil;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.widget.codeView.CodeView;
import kualian.dc.deal.application.widget.codeView.KeyboardView;

/**
 * Created by idmin on 2018/2/24.
 */

public class WalletPassWord extends SourceDelegate {
    private boolean isComplete, isRestore;
    private static final String KEY = "key";
    private static final String SEED = "seed";
    private String seed, key;
    private WalletDao walletDao =new WalletDao();
    public static WalletPassWord getInstance(boolean isRestore, String seed) {
        WalletPassWord walletPassWord = new WalletPassWord();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY, isRestore);
        bundle.putString(SEED, seed);
        walletPassWord.setArguments(bundle);
        return walletPassWord;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_pw;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View contentView) {
        super.onBindView(savedInstanceState, contentView);
        if (getArguments() != null) {
            isRestore = getArguments().getBoolean(KEY);
            seed = getArguments().getString(SEED);
        }
        TextView ivClose = contentView.findViewById(R.id.close_pass);
        final TextView textView = contentView.findViewById(R.id.button_next);
        textView.setVisibility(View.VISIBLE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isComplete) {
                    String seedId = KeyUtil.genWalletIdFromSeed(seed);
                    SpUtil.getInstance().setWalletSend(key, seed);
                    SpUtil.getInstance().setWalletID(seedId);
                    DataBaseUtil.deleteDataWithId(seedId);
                    try {
                        walletDao.deleteWithId(seedId);
                        walletDao.add(KeyUtil.genWalletIdFromSeed(seed),SpUtil.getInstance().getWalletName(),SpUtil.getInstance().getWalletPw(),SpUtil.getInstance().getLoginPw(),2, AesUtil.encrypt(key, seed));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    start(CreateWallet.getInstance(false,key));
                }
            }
        });
        contentView.findViewById(R.id.wallet_pw_tip).setVisibility(View.VISIBLE);
        ivClose.setVisibility(View.GONE);
        final TextView title = contentView.findViewById(R.id.wallet_pw_title);
        title.setText(R.string.account_pw);

        final KeyboardView keyboardView = contentView.findViewById(R.id.password_input);
        CodeView codeView = contentView.findViewById(R.id.password_view);
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
                isComplete = false;
                textView.setBackground(getResources().getDrawable(R.drawable.bg_gray));
            }

            @Override
            public void onComplete(String value) {
                // TODO: 2017/2/5
                isComplete = true;
                key = value;
                textView.setBackground(getResources().getDrawable(R.drawable.bg_blue));
                SpUtil.getInstance().setWalletPw(value);

            }
        });
    }
}
