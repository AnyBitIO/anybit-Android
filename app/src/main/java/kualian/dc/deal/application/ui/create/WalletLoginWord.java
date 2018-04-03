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
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.widget.codeView.CodeView;
import kualian.dc.deal.application.widget.codeView.KeyboardView;

/**
 * Created by idmin on 2018/2/24.
 */

public class WalletLoginWord extends SourceDelegate {
    private boolean isComplete;
    private static final String KEY = "key";
    private static final String SEED = "seed";
    private boolean isRestore;
    private String seed;

    public static WalletLoginWord getInstance(boolean isRestore, String seed) {
        WalletLoginWord walletPassWord = new WalletLoginWord();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY, isRestore);
        bundle.putString(SEED, seed);
        walletPassWord.setArguments(bundle);
        return walletPassWord;
    }

    @Override
    public Object setLayout() {
        return R.layout.widget_code;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View contentView) {
        super.onBindView(savedInstanceState, contentView);
        isRestore = getArguments().getBoolean(KEY, false);
        seed = getArguments().getString(SEED, null);
        TextView ivClose = contentView.findViewById(R.id.close_pass);
        final TextView textView = contentView.findViewById(R.id.button_next);
        textView.setVisibility(View.VISIBLE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isComplete) {
                    start(WalletPassWord.getInstance(isRestore,seed));
                    //_mActivity.onBackPressed()
                }
            }
        });
        TextView tip = contentView.findViewById(R.id.wallet_pw_tip);
        tip.setVisibility(View.VISIBLE);
        tip.setText(getString(R.string.account_login_tip));
        ivClose.setVisibility(View.GONE);
        final TextView title = contentView.findViewById(R.id.wallet_pw_title);
        title.setText(R.string.account_login_pw);

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
                textView.setBackground(getResources().getDrawable(R.drawable.bg_blue));
                SpUtil.getInstance().setLoginPw(value);

            }
        });
    }
}
