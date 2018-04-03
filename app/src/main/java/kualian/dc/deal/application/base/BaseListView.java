package kualian.dc.deal.application.base;

/**
 * Created by zheng on 2017/12/25.
 */

public interface BaseListView<P> extends BaseView{
    void showLoadingView();
    void hideLoadingView();
}
