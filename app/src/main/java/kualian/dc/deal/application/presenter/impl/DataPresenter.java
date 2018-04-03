package kualian.dc.deal.application.presenter.impl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhouyou.http.RxHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.utils.RxUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.time.chrono.IsoChronology;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.bean.ResponseCode;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.bean.ResultCode;
import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.presenter.home.HomeContract;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.NetUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by idmin on 2018/3/6.
 */

public class DataPresenter {

    public HomeContract.View mView;
    public ResponseData result;
    public DataPresenter(HomeContract.View mView) {
        this.mView = mView;
        result=new ResponseData();
    }
    public void queryServiceData(String json, String tag){
        boolean available = NetUtil.isNetworkAvailable(WalletApp.getContext());
        if (!available&&!tag.equals(Constants.tran_build)){
            //Toast.makeText(WalletApp.getContext(),R.string.view_no_network,Toast.LENGTH_LONG).show();
            RxToast.showToast(R.string.view_no_network);
        }
        result=new ResponseData();
        String url = Constants.OFFICIAL;
        OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(json)
                .build()
                .execute(new Callback<String>() {
                    @Override
                    public String parseNetworkResponse(Response data, int id) throws Exception {
                        String response =null;
                        try {
                            response =data.body().string();
                            ResponseCode responseCode = new Gson().fromJson(response, ResponseCode.class);
                            if (responseCode!=null&&responseCode.getRtnCode()!=1){
                                RxToast.showToast(responseCode.getErrMsg());
                            }
                        }catch (Exception e){
                            if (!tag.equals(Constants.update_query)){
                               // RxToast.showToast(R.string.service_fail);
                            }
                        }
                        result.setResponse(response);
                        result.setApiException(null);
                        mView.getServiceData(result,tag);

                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (available){
                            if (!tag.equals(Constants.update_query)){
                                RxToast.showToast(R.string.service_fail);
                            }
                        }
                        AppLoader.stopLoading();
                        result.setApiException(e);
                        ResponseCode resultCode=new ResponseCode();
                        resultCode.setRtnCode(Constants.RTNCODE_ERROR);
                        resultCode.setErrMsg("");
                        resultCode.setErrCode(Constants.ERRCODE_ERROR);
                        result.setResponse(new Gson().toJson(resultCode));
                        mView.getServiceData(result,tag);

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });

       /* RxHttp.post("server/process")
                .upJson(json)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtils.i("RxHttp = "+e.toString());

                        //showToast(e.getMessage()+"  "+e.getCode());
                    }

                    @Override
                    public void onSuccess(String response) {
                        try {
                            ResponseCode responseCode = new Gson().fromJson(response, ResponseCode.class);
                            if (responseCode!=null&&responseCode.getRtnCode()!=1){
                                RxToast.showToast(responseCode.getErrMsg());
                            }
                        }catch (Exception e){
                            if (!tag.equals(Constants.update_query)){
                                RxToast.showToast(R.string.service_fail);
                            }
                        }
                        result.setResponse(response);
                        result.setApiException(null);
                        mView.getServiceData(result,tag);
                    }
                });*/
    }
}
