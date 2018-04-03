package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.dialog.RxDialogSureCancel;
import kualian.dc.deal.application.web.WebDelegateHead;

/**
 * Created by idmin on 2018/3/13.
 */

public class AboutUsDelegate extends SourceDelegate {
    private TextView mDeal,mSecret,mContact,mShare;
    @Override
    public Object setLayout() {
        return R.layout.delegate_about_us;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        mDeal = rootView.findViewById(R.id.deal);
        mSecret = rootView.findViewById(R.id.secret);
        mContact = rootView.findViewById(R.id.connect);
        mShare = rootView.findViewById(R.id.share);
        findToolBar(rootView);
        toolTitle.setText(R.string.setting_about_us);
        setOnClickViews(mDeal,mSecret,mContact,mShare,toolBack);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.deal:
                start(WebDelegateHead.getInstance(Constants.WEB_SERVICE,getResources().getString(R.string.setting_deal),true));
                break;
            case R.id.connect:
                RxDialogSureCancel dialog=new RxDialogSureCancel(getContext());
                dialog.setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonUtil.copyContent(_mActivity,dialog.getContentView().getText().toString());
                        RxToast.showToast(R.string.tips_copy);
                        dialog.cancel();
                    }
                });
                dialog.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
            case R.id.share:
                start(new ShareDelegate());
                break;
            case R.id.secret:
                start(WebDelegateHead.getInstance(Constants.WEB_PRIVACY,getResources().getString(R.string.setting_secret),true));
                break;
            case R.id.toolbar_back:
                _mActivity.onBackPressed();
                break;
        }
    }
}
