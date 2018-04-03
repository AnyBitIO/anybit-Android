package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.LogBean;
import kualian.dc.deal.application.widget.DivItemDecoration;

/**
 * Created by idmin on 2018/3/13.
 */

public class UpDateLogDelegate extends SourceDelegate {
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private List<LogBean> logBeans = new ArrayList<>();

    @Override
    public Object setLayout() {
        return R.layout.delegate_update_log;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        findToolBar(rootView);
        mRecyclerView = rootView.findViewById(R.id.recycle_log);
        toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
        toolTitle.setVisibility(View.VISIBLE);
        toolTitle.setText(R.string.setting_update_tips);

        String[] versions = getResources().getStringArray(R.array.log_version);
        String[] contents = getResources().getStringArray(R.array.log_content);
        for (int i = 0; i < versions.length; i++) {
            LogBean logBean = new LogBean();
            logBean.setLogContent(contents[i]);
            logBean.setLogVersion(versions[i]);
            logBeans.add(logBean);
        }
        mAdapter = new BaseQuickAdapter<LogBean, BaseViewHolder>(R.layout.item_log_bean, logBeans) {
            @Override
            protected void convert(BaseViewHolder helper, LogBean item) {
                helper.setText(R.id.log_version,item.getLogVersion());
                ImageView icon = helper.getView(R.id.log_down);
                TextView content = helper.getView(R.id.log_content);
                content.setText(item.getLogContent());
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.isOpen()) {
                            content.setVisibility(View.GONE);
                            item.setOpen(false);
                            icon.setImageResource(R.drawable.ic_down_icon);
                        } else {
                            item.setOpen(true);
                            content.setVisibility(View.VISIBLE);
                            icon.setImageResource(R.drawable.ic_up_coin);
                        }
                    }
                });
            }
        };
        mAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }
}
