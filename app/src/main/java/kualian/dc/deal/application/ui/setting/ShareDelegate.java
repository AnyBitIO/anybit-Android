package kualian.dc.deal.application.ui.setting;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.CaseFormat;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.RxToast;

/**
 * Created by idmin on 2018/3/22.
 */

public class ShareDelegate extends SourceDelegate{
    private TextView copy;

    @Override
    public Object setLayout() {
        return R.layout.delegate_share;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        findToolBar(rootView);
        toolTitle.setText(R.string.setting_share);
        copy = rootView.findViewById(R.id.copy);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_share);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        toolNext.setCompoundDrawables(drawable, null, null, null);
        toolNext.setVisibility(View.VISIBLE);

        setOnClickViews(toolBack,copy,toolNext);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.toolbar_back:
                _mActivity.onBackPressed();
                break;
            case R.id.toolbar_next:
                Intent shareIntent = new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.setting_download));
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
                break;
            case R.id.copy:
                CommonUtil.copyContent(_mActivity,getResources().getString(R.string.setting_download));
                RxToast.showToast(R.string.tips_copy);
                break;
        }
    }
}
