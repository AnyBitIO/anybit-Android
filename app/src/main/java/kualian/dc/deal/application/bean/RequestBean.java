package kualian.dc.deal.application.bean;

import com.zhouyou.http.utils.RxUtil;

import org.bouncycastle.jcajce.provider.digest.MD5;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import kualian.dc.deal.application.ui.setting.SettingDelegate;
import kualian.dc.deal.application.util.AppTools;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.WalletTool;

/**
 * Created by idmin on 2018/3/5.
 */

public class RequestBean implements Serializable {

    /**
     * header : {"version":"1.0.1","language":"zh","trancode":"wallet_create","clienttype":"Android","walletid":"mvCKSmXu5Dhy7DWKXU3dpwqVcvuZNoqnlm","random":"123456","handshake":"abcdefg","imie":"abcdefg"}
     * body : {"addrs":[{"coinAddr":"xxx","coinType":"yyy"},{"coinAddr":"mmm","coinType":"nnn"}]}
     */
    public RequestBean() {

    }

    private HeaderBean header;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }


    public static class HeaderBean implements Serializable {
        public HeaderBean(String mRandom) {
            this.version = SpUtil.getInstance().getDefaultVersion();
            if (SpUtil.getInstance().getDefaultLanguage() == null) {
                this.language = "zh";
            } else {
                this.language = SpUtil.getInstance().getDefaultLanguage();

            }
            this.trancode = Constants.TRANCODE;
            this.clienttype = "Android";
            this.walletid = SpUtil.getInstance().getWalletID();
            this.random = mRandom;
            this.handshake = WalletTool.Md5(Constants.SECRET.replace("@", mRandom));
            this.imie = "imie";
        }

        /**
         * version : 1.0.1
         * language : zh
         * trancode : wallet_create
         * clienttype : Android
         * walletid : mvCKSmXu5Dhy7DWKXU3dpwqVcvuZNoqnlm
         * random : 123456
         * handshake : abcdefg
         * imie : abcdefg
         */

        private String version;
        private String language;
        private String trancode;
        private String clienttype;
        private String walletid;
        private String random;
        private String handshake;
        private String imie;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getTrancode() {
            return trancode;
        }

        public void setTrancode(String trancode) {
            this.trancode = trancode;
        }

        public String getClienttype() {
            return clienttype;
        }

        public void setClienttype(String clienttype) {
            this.clienttype = clienttype;
        }

        public String getWalletid() {
            return walletid;
        }

        public void setWalletid(String walletid) {
            this.walletid = walletid;
        }

        public String getRandom() {
            return random;
        }

        public void setRandom(String random) {
            this.random = random;
        }

        public String getHandshake() {
            return handshake;
        }

        public void setHandshake(String handshake) {
            this.handshake = handshake;
        }

        public String getImie() {
            return imie;
        }

        public void setImie(String imie) {
            this.imie = imie;
        }
    }


}
