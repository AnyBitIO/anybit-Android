package kualian.dc.deal.application.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.callback.OnCloseDelegateListener;
import kualian.dc.deal.application.ui.create.RestoreDelegate;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.widget.codeView.CodeView;
import kualian.dc.deal.application.widget.codeView.KeyboardView;

/**
 * Created by idmin on 2018/3/1.
 */

public class DelegateLogin extends SourceDelegate {
    private OnCloseDelegateListener onCreateWalletListener;

    @Override
    public Object setLayout() {
        return R.layout.delegate_login;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnCloseDelegateListener) {
            onCreateWalletListener = (OnCloseDelegateListener) activity;
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View contentView) {
        super.onBindView(savedInstanceState, contentView);
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

            }

            @Override
            public void onComplete(String value) {
                // TODO: 2017/2/5
                if (KeyUtil.getPwMessage(value).equals(SpUtil.getInstance().getLoginPw())) {
                   /* if (getSupportDelegate().findFragment(MainDelegate.class) == null) {
                    }*/
                    if (onCreateWalletListener != null) {
                        onCreateWalletListener.OnCloseDelegate();
                    }

                } else {
                    /*LogUtils.i("codeview "+codeView);
                    keyboardView.clear();*/
                    RxToast.showToast(" "+codeView);

                   // RxToast.showToast(getResources().getString(R.string.error_pw));
                }
            }
        });
        contentView.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(RestoreDelegate.newInstance(null));
            }
        });
    }
}
