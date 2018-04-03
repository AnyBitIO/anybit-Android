package kualian.dc.deal.application.ui.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import java.util.ArrayList;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.ui.camera.RequestCodes;
import kualian.dc.deal.application.ui.scan.ActivityScanerCode;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.util.dialog.RxDialogSureCancel;

/**
 * Created by idmin on 2018/2/11.
 */

public class RestoreDelegate extends SourceDelegate {
    private MultiAutoCompleteTextView mnemonicTextView;
    private ImageButton scan;
    private Button btn;
    private String seed;
    private TextView error, skip, mTitle, mBack;
    private boolean isNewSeed;
    private ArrayList<String> seedWords;
    public static RestoreDelegate newInstance(String seed) {
        RestoreDelegate fragment = new RestoreDelegate();
        if (seed != null) {
            Bundle args = new Bundle();
            args.putString(Constants.ARG_SEED, seed);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_restore;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        mnemonicTextView = rootView.findViewById(R.id.seed);
        scan = rootView.findViewById(R.id.scan_qr_code);
        error = rootView.findViewById(R.id.restore_message);
        mTitle = rootView.findViewById(R.id.title);
        mBack = rootView.findViewById(R.id.back);
        skip = rootView.findViewById(R.id.skip);
        if (getArguments() != null) {
            seed = getArguments().getString(Constants.ARG_SEED);
            isNewSeed = seed != null;
            if (!isNewSeed) {
                mBack.setVisibility(View.VISIBLE);
                skip.setVisibility(View.GONE);
                mTitle.setText(R.string.restore_input);
            }
        }
        btn = rootView.findViewById(R.id.button_next);
        setOnClickViews(scan, skip, btn, mBack);
        mnemonicTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                error.setVisibility(View.INVISIBLE);
                mnemonicTextView.setBackground(getResources().getDrawable(R.drawable.bg));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    btn.setBackground(getResources().getDrawable(R.drawable.bg_blue));
                    btn.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btn.setBackground(getResources().getDrawable(R.drawable.bg_white));
                    btn.setTextColor(getResources().getColor(R.color.text_gray));
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.scan_qr_code) {
            startActivityForResult(new Intent(_mActivity, ActivityScanerCode.class), RequestCodes.SCAN);
            CallbackManager.getInstance()
                    .addCallback(CallbackType.ON_SCAN, new IGlobalCallback<String>() {
                        @Override
                        public void executeCallback(@Nullable String args) {
                            mnemonicTextView.setText(args);
                        }
                    });
        } else if (view.getId() == R.id.skip) {
            if (isNewSeed) {
                start(WalletLoginWord.getInstance(false,seed));
                CommonUtil.hideSoftInput(_mActivity);
            } else {
                CommonUtil.hideSoftInput(_mActivity);
                _mActivity.onBackPressed();
            }

        } else if (view.getId() == R.id.back) {
            CommonUtil.hideSoftInput(_mActivity);
            _mActivity.onBackPressed();
        } else if (view.getId() == R.id.button_next) {
            if (isNewSeed) {
                if (mnemonicTextView.getText().toString().equals(seed)) {
                    CommonUtil.hideSoftInput(_mActivity);
                    start(WalletLoginWord.getInstance(false,seed));
                } else {
                    error.setVisibility(View.VISIBLE);
                    mnemonicTextView.setBackground(getResources().getDrawable(R.drawable.bg_red_side));
                }
            } else {
                if (verifyMnemonic()) {
                    String words=null;
                    if (seedWords!=null){
                       words  = KeyUtil.getSendWords(seedWords);
                    }
                    CommonUtil.hideSoftInput(_mActivity);
                    start(WalletName.getInstance(words));
                }
            }
        }
    }

    private boolean verifyMnemonic() {
        boolean isSeedValid = false;
        String seedText = mnemonicTextView.getText().toString().trim();
        seedWords = KeyUtil.parseMnemonic(seedText);
       /* if (CommonUtil.isNumAndLetter(seedWords)&&seedWords.size()==18){
            isSeedValid =true;
        }else {
            setError(error, R.string.restore_error_checksum);
        }*/
        try {
            MnemonicCode.INSTANCE.check(seedWords);
            isSeedValid = true;
        } catch (MnemonicException.MnemonicChecksumException e) {
            setError(error, R.string.restore_error_checksum);
        } catch (MnemonicException.MnemonicWordException e) {
            setError(error, R.string.restore_error_checksum);
        } catch (MnemonicException e) {
            setError(error, R.string.restore_error_checksum);
        }
        return isSeedValid;
    }

    private void setError(TextView error, int restore_error_checksum) {
        error.setVisibility(View.VISIBLE);
    }
}
