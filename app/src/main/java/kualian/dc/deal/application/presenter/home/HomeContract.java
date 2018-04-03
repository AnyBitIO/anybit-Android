package kualian.dc.deal.application.presenter.home;

import java.util.WeakHashMap;

import kualian.dc.deal.application.base.BaseModel;
import kualian.dc.deal.application.base.BasePresenter;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.bean.ResponseData;

/**
 * Created by idmin on 2018/3/5.
 */

public interface HomeContract {
    interface View extends BaseView{
        void getServiceData(ResponseData response,String tag);
    }
    interface  Model extends BaseModel {
        ResponseData parseData(String json);
    }
    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void queryServiceData(String json,String tag);

    }
}
