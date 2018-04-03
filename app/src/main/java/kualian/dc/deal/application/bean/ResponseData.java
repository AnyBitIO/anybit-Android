package kualian.dc.deal.application.bean;

import com.zhouyou.http.exception.ApiException;

/**
 * Created by idmin on 2018/3/5.
 */

public class ResponseData  {

    private String response;
    private Exception apiException;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Exception getApiException() {
        return apiException;
    }

    public void setApiException(Exception apiException) {
        this.apiException = apiException;
    }
}
