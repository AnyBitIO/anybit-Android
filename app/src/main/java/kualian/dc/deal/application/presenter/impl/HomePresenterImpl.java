package kualian.dc.deal.application.presenter.impl;

import kualian.dc.deal.application.presenter.home.HomeContract;

/**
 * Created by idmin on 2018/3/5.
 */

public class HomePresenterImpl extends HomeContract.Presenter {

    @Override
    public void queryServiceData(String json,String tag) {
        mView.getServiceData(mModel.parseData(json),tag);
        //mView.getServiceData(mModel.parseData());
    }
}
