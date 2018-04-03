package kualian.dc.deal.application.util;

import android.content.Context;

import kualian.dc.deal.application.update.UpdateAgent;
import kualian.dc.deal.application.update.UpdateInfo;
import kualian.dc.deal.application.update.UpdateManager;


/**
 * Created by zheng on 2017/12/27.
 */

public class UpdateUtil {
    public static void check(Context context, boolean isManual, final boolean hasUpdate, final boolean isForce, final boolean isSilent, final boolean isIgnorable, final int notifyId) {
        UpdateManager.create(context).setUrl(SpUtil.getInstance().getUpdateUrl()).setManual(isManual).setNotifyId(notifyId).setParser(new UpdateAgent.InfoParser() {
            @Override
            public UpdateInfo parse(String source) throws Exception {
                UpdateInfo info = new UpdateInfo();
                info.hasUpdate = hasUpdate;
                info.updateContent = "102";
                info.versionCode = 1000;
                info.size = 10002;
                info.versionName = SpUtil.getInstance().getVersion();
                info.url = SpUtil.getInstance().getUpdateUrl();
                info.md5 = WalletTool.Md5("6658889");
                info.isForce = isForce;
                info.isIgnorable = isIgnorable;
                info.isSilent = isSilent;
                return info;
            }
        }).check();
    }
}
