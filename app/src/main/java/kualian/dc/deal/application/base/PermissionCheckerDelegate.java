package kualian.dc.deal.application.base;

import android.content.Intent;

import kualian.dc.deal.application.ui.camera.RequestCodes;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;


/**
 * Created by zheng on 2017/12/15.
 */

public abstract class PermissionCheckerDelegate<M extends BaseModel, P extends BasePresenter> extends BaseDelegate<M, P> {
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.SCAN:
                   /* //拿到剪裁后的数据进行处理
                    @SuppressWarnings("unchecked") final IGlobalCallback<String> callback = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.ON_CROP);
                    if (callback != null) {
                        callback.executeCallback(data.getStringExtra(ScanActivity.INTENT_EXTRA_RESULT));
                    }*/
                    break;

                default:
                    break;
            }
        }
    }
}
