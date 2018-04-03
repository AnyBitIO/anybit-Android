package kualian.dc.deal.application.presenter.logic;

import com.zhouyou.http.RxHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.presenter.home.HomeContract;
import kualian.dc.deal.application.util.RxToast;

/**
 * Created by idmin on 2018/3/5.
 */

public class HomeLogic implements HomeContract.Model {
    ResponseData result;
    @Override
    public ResponseData parseData(String json) {
        result=new ResponseData();
        RxHttp.post("server/process")
                .upJson(json)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                       result.setApiException(e);
                       result.setResponse(null);

                        //showToast(e.getMessage()+"  "+e.getCode());
                    }

                    @Override
                    public void onSuccess(String response) {
                        result.setResponse(response);
                        result.setApiException(null);
                    }
                });

        return result;
    }
}
