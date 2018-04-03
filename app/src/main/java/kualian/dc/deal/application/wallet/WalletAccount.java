package kualian.dc.deal.application.wallet;

import java.io.Serializable;
import java.util.List;

/**
 * Created by idmin on 2018/2/23.
 */

public class WalletAccount {
    public  String walletName;
    public  String walletId;
    public  String walletHead;
    public  String walletSend;

    public String getWalletHead() {
        return walletHead;
    }

    public void setWalletHead(String walletHead) {
        this.walletHead = walletHead;
    }

    public String getWalletSend() {
        return walletSend;
    }

    public void setWalletSend(String walletSend) {
        this.walletSend = walletSend;
    }

    public  String walletLoginPw;
    public  String walletTradePw;
    public  int walletDefaultIndex;

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getWalletLoginPw() {
        return walletLoginPw;
    }

    public void setWalletLoginPw(String walletLoginPw) {
        this.walletLoginPw = walletLoginPw;
    }

    public String getWalletTradePw() {
        return walletTradePw;
    }

    public void setWalletTradePw(String walletTradePw) {
        this.walletTradePw = walletTradePw;
    }

    public int getWalletDefaultIndex() {
        return walletDefaultIndex;
    }

    public void setWalletDefaultIndex(int walletDefaultIndex) {
        this.walletDefaultIndex = walletDefaultIndex;
    }
}
