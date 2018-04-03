package kualian.dc.deal.application.wallet.coins;


import kualian.dc.deal.application.R;
import kualian.dc.deal.application.wallet.CoinIndex;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * @author John L. Jegutanis
 */
public class TVMain extends CoinType {
    public TVMain() {
        id = "tv.main";

        addressHeader = 0;
        p2shHeader = 5;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 100;
        dumpedPrivateKeyHeader = 128;

        coinName = "TV";
        coinIndex= CoinIndex.TVCOIN.index;
        coinResource= R.drawable.uro;
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
