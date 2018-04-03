package kualian.dc.deal.application.ui.create;

import android.os.AsyncTask;


import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.MainNetParams;

import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.coins.TVMain;
import kualian.dc.deal.application.wallet.coins.UbMain;

/**
 * @author John L. Jegutanis
 */
public final class AddCoinTask extends AsyncTask<Void, Void, Void> {
    private final Listener listener;
    private CoinDao coinDao;
    private String mKey;
    private DeterministicKey masterKey ;
    public interface Listener {
        void onAddCoinTaskFinished();
    }

    public AddCoinTask(Listener listener, CoinDao coinDao, String key) {
        this.listener = listener;
        this.coinDao = coinDao;
        this.mKey = key;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... params) {
        UbMain coinType = new UbMain();
        masterKey=KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(mKey));
        coinType.setCoinAddress(KeyUtil.genSubPubAddrWifFromMasterKey(masterKey, coinType.getCoinIndex(), MainNetParams.get()));
        //TVMain tvMain = new TVMain();
        //tvMain.setCoinAddress(KeyUtil.genTVAddress(KeyUtil.genSubKeyFromMasterKey(masterKey,tvMain.getCoinIndex())));
        coinDao.add(coinType);
       // coinDao.add(tvMain);
        return null;
    }

    @Override
    final protected void onPostExecute(Void aVoid) {
        listener.onAddCoinTaskFinished();
    }
}