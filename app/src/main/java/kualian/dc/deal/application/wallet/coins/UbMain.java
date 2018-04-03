package kualian.dc.deal.application.wallet.coins;


import org.bitcoinj.params.MainNetParams;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.CoinIndex;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * @author John L. Jegutanis
 */
public class UbMain extends CoinType {
    public UbMain() {
        id = "ub.main";

        addressHeader = 0;
        p2shHeader = 5;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 100;
        dumpedPrivateKeyHeader = 128;

        coinName = "UBTC";
        coinIndex= CoinIndex.UBCOIN.index;
        coinResource= R.drawable.coin_ubtc;
       /* if (key!=null){
            coinAddress= KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(key)), coinIndex, MainNetParams.get());
            SpUtil.getInstance().setWalletAddress(coinAddress);

        }*/
    }

   /* private static UbMain instance = new UbMain();
    public static   UbMain get() {
        return instance;
    }*/
}
