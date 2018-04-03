package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.UpdateUtil;

/**
 * Created by idmin on 2018/3/9.
 */

public class CheckUpdateDelegate extends SourceDelegate{
    private TextView update,version,isNew,log,newUpdate;
    private boolean isCanUpdate;
    @Override
    public Object setLayout() {
        return R.layout.delegate_update;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        findToolBar(rootView);
        toolTitle.setVisibility(View.VISIBLE);
        toolTitle.setText(R.string.setting_update);
        update = rootView.findViewById(R.id.update);

        log = rootView.findViewById(R.id.log);
        version = rootView.findViewById(R.id.version);
        isNew = rootView.findViewById(R.id.update_is_new);
        newUpdate = rootView.findViewById(R.id.update_new);
        setOnClickViews(toolBack,update,log);
        String s = getResources().getString(R.string.setting_version_now);
        String content=s+SpUtil.getInstance().getDefaultVersion();
        version.setText(content);
        if (!SpUtil.getInstance().getDefaultVersion().equals(SpUtil.getInstance().getVersion())){
            isCanUpdate = true;
            newUpdate.setVisibility(View.VISIBLE);
            isNew.setVisibility(View.INVISIBLE);
        }else {
            isCanUpdate=false;
            isNew.setText(R.string.setting_update_new_now);

        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.log:
                start(new UpDateLogDelegate());
                break;
            case R.id.update:
                if (isCanUpdate){
                    if (getContext().getExternalFilesDir("") != null) {
                        UpdateUtil.check(getContext(), false, true, false, false, false, 998);
                    }
                    //UpdateUtil.check(getContext(),true,true,false,false,false,998);
                }
                break;
            case R.id.toolbar_back:
                _mActivity.onBackPressed();
                break;
        }
       /* if (view.getId()==R.id.update){
            UpdateUtil.check(getContext(),true,true,false,false,false,998);
        }else {
            _mActivity.onBackPressed();
        }*/
    }
}
