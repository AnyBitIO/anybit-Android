package kualian.dc.deal.application.wallet.coins;


import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.CoinIndex;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * @author FuzzyHobbit
 */
public class UroMain extends CoinType {
    private UroMain() {
        id = "uro.main";

        addressHeader = 68;
        p2shHeader = 5;
        acceptableAddressCodes = new int[]{addressHeader, p2shHeader};
        spendableCoinbaseDepth = 20;

        coinName = "Uro";
        coinIndex = CoinIndex.URO.index;
        coinResource= R.drawable.uro;
        //coinAddress= KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend()), coinIndex, new MainNetParams());

    }

    private static UroMain instance = new UroMain();

    public static synchronized CoinType get() {
        return instance;
    }
}
