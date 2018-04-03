package kualian.dc.deal.application.ui.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;


import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.ui.account.DelegateVerify;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by idmin on 2018/2/11.
 */

public class SeedDelegate extends SourceDelegate {
    TextView seed;
    private String seedContent;
    public static SeedDelegate getInstance(boolean isCreate) {
        SeedDelegate delegate=new SeedDelegate();
        Bundle bundle=new Bundle();
        bundle.putBoolean("tip",isCreate);
        delegate.setArguments(bundle);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_seed;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        seed=rootView.findViewById(R.id.seed_content);
        seedContent=KeyUtil.getSeedWordStr();
        seed.setText(seedContent);
        seed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seedContent=KeyUtil.getSeedWordStr();
                seed.setText(seedContent);
                //CommonUtil.copyContent(_mActivity,seedContent);
            }
        });
        rootView.findViewById(R.id.seed_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(DelegateVerify.getInstance(seed.getText().toString()));
            }
        });
    }

}
