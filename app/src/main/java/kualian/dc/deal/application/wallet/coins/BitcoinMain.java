package kualian.dc.deal.application.wallet.coins;


import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.CoinIndex;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * @author John L. Jegutanis
 */
public class BitcoinMain extends CoinType {
    private BitcoinMain() {
        id = "bitcoin.main";

        addressHeader = 0;
        p2shHeader = 5;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 100;
        dumpedPrivateKeyHeader = 128;

        coinName = "BTC";
        coinIndex= CoinIndex.BITCOIN.index;
        coinResource= R.drawable.coin_btc;
        /*if (SpUtil.getInstance().getWalletSend()!=null){
            coinAddress= KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend()), coinIndex,  MainNetParams.get());
        }*/

    }

    private static BitcoinMain instance = new BitcoinMain();
    public static  CoinType get() {
        return instance;
    }
}
