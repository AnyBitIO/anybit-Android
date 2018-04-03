package kualian.dc.deal.application.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.net.ConnectException;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.util.RxToast;


/**
 * Created by Meiji on 2017/6/10.
 */

public class BaseListDelegate<M extends BaseModel, P extends BasePresenter> extends SourceDelegate<M, P> implements BaseListView
        , SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener,BaseQuickAdapter.RequestLoadMoreListener {
    protected BaseQuickAdapter mAdapter;
    public static final String TAG = "BaseListFragment";
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager linearLayoutManager;
    protected SwipeRefreshLayout smartRefreshLayout;
    protected TextView mTitle,mNext,mBack,mHead;
    protected ViewStub viewStub;
    protected TextView empty;
    @Override
    protected void onEvent() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public Object setLayout() {
        return R.layout.universal_list;

    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recycle);
        smartRefreshLayout = rootView.findViewById(R.id.refresh);
        mTitle = rootView.findViewById(R.id.toolbar_title);
        mNext = rootView.findViewById(R.id.toolbar_next);
        mBack = rootView.findViewById(R.id.toolbar_back);
        mHead = rootView.findViewById(R.id.head_tips);
        viewStub = rootView.findViewById(R.id.view_stub);
        //setOnClickViews(mBack);
        //viewStub.setLayoutResource();
    }

    public void bindEmptyView(){
        mAdapter.setEmptyView(R.layout.view_empty);
        empty=mAdapter.getEmptyView().findViewById(R.id.empty_text);
    }
    @Override
    public void showLoadingView() {
        AppLoader.showLoading(getActivity());
    }

    @Override
    public void hideLoadingView() {
        mAdapter.setEmptyView(R.layout.view_empty);
        AppLoader.stopLoading();
    }

    @Override
    public void showErrorWithStatus(Throwable msg) {
        if (msg instanceof ConnectException) {
            RxToast.showToast(getResources().getString(R.string.view_network_error));
        } else {
            RxToast.showToast(msg.toString());
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.toolbar_back){
            _mActivity.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMoreRequested() {

    }
}
