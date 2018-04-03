package kualian.dc.deal.application.ui.account;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*import com.coinomi.core.coins.CoinID;
import com.coinomi.core.coins.CoinType;
import com.coinomi.core.wallet.Wallet;*/


import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.config.CoinService;
import kualian.dc.deal.application.config.CoinServiceImpl;
import kualian.dc.deal.application.util.WeakHandler;

/**
 * Fragment that restores a wallet
 */
public class WalletRestorationDelegate extends SourceDelegate {
    private static final Logger log = LoggerFactory.getLogger(WalletRestorationDelegate.class);

    private static final int RESTORE_STATUS_UPDATE = 0;
    private static final int RESTORE_FINISHED = 1;

    private final Handler handler = new MyHandler(this,_mActivity);

    // FIXME: Ugly hack to keep a reference to the task even if the fragment is recreated
   // private static WalletFromSeedTask walletFromSeedTask;
    private TextView status;


    /**
     * Get a fragment instance.
     */
    public static WalletRestorationDelegate newInstance(Bundle args) {
        WalletRestorationDelegate fragment = new WalletRestorationDelegate();
        fragment.setRetainInstance(true);
        fragment.setArguments(args);
        return fragment;
    }

    public WalletRestorationDelegate() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WalletApp app = getWalletApplication();
       /* if (getArguments() != null) {
            Bundle args = getArguments();
            String seed = args.getString(WalletConstants.ARG_SEED);
            String password = args.getString(WalletConstants.ARG_PASSWORD);
            String seedPassword = args.getString(WalletConstants.ARG_SEED_PASSWORD);
            List<CoinType> coinsToCreate = getCoinsTypes(args);

            if (walletFromSeedTask == null) {
                walletFromSeedTask = new WalletFromSeedTask(handler, app, coinsToCreate, seed, password, seedPassword);
                walletFromSeedTask.execute();
            } else {
                switch (walletFromSeedTask.getStatus()) {
                    case FINISHED:
                        handler.sendEmptyMessage(RESTORE_FINISHED);
                        break;
                    case RUNNING:
                    case PENDING:
                        walletFromSeedTask.handler = handler;
                }
            }
        }*/
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_wallet_restoration;
    }

    @Override
    public void onBindView(@android.support.annotation.Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        status = (TextView) rootView.findViewById(R.id.restoration_status);
    }

  /*  private List<CoinType> getCoinsTypes(Bundle args) {
        ArrayList<String> coinIds = args.getStringArrayList(WalletConstants.ARG_MULTIPLE_COIN_IDS);
        if (coinIds != null) {
            List<CoinType> coinTypes = new ArrayList<CoinType>();
            for (String id : coinIds) {
                coinTypes.add(CoinID.typeFromId(id));
            }
            return coinTypes;
        } else {
            return WalletConstants.DEFAULT_COINS;
        }
    }*/

    WalletApp getWalletApplication() {
        return (WalletApp) getActivity().getApplication();
    }

    /*static class WalletFromSeedTask extends AsyncTask<Void, String, Wallet> {
        Wallet wallet;
        String errorMessage = "";
        private final String seed;
        private final String password;
        @Nullable
        private final String seedPassword;
        Handler handler;
        private final WalletApp walletApp;
        private final List<CoinType> coinsToCreate;

        public WalletFromSeedTask(Handler handler, WalletApp walletApp, List<CoinType> coinsToCreate, String seed, String password, @Nullable String seedPassword) {
            this.handler = handler;
            this.walletApp = walletApp;
            this.coinsToCreate = coinsToCreate;
            this.seed = seed;
            this.password = password;
            this.seedPassword = seedPassword;
        }

        protected Wallet doInBackground(Void... params) {
            Intent intent = new Intent(CoinService.ACTION_CLEAR_CONNECTIONS, null,
                    walletApp, CoinServiceImpl.class);
            walletApp.startService(intent);

            ArrayList<String> seedWords = new ArrayList<String>();
            for (String word : seed.trim().split(" ")) {
                if (word.isEmpty()) continue;
                seedWords.add(word);
            }

            try {
                this.publishProgress("");
                walletApp.setEmptyWallet();
                wallet = new Wallet(seedWords, seedPassword);
                KeyParameter aesKey = null;
                if (password != null && !password.isEmpty()) {
                    KeyCrypterScrypt crypter = new KeyCrypterScrypt();
                    aesKey = crypter.deriveKey(password);
                    wallet.encrypt(crypter, aesKey);
                }

                for (CoinType type : coinsToCreate) {
                    this.publishProgress(type.getName());
                    wallet.createAccount(type, false, aesKey);
                }

                walletApp.setWallet(wallet);
                walletApp.saveWalletNow();
            } catch (Exception e) {
                log.error("Error creating a wallet", e);
                errorMessage = e.getMessage();
            }
            return wallet;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            handler.sendMessage(handler.obtainMessage(RESTORE_STATUS_UPDATE, values[0]));
        }

        protected void onPostExecute(Wallet wallet) {
            handler.sendEmptyMessage(RESTORE_FINISHED);
        }
    }*/

    private static class MyHandler extends WeakHandler<WalletRestorationDelegate> {
        private FragmentActivity _mActivity;

        public MyHandler(WalletRestorationDelegate ref,FragmentActivity _mActivity) {
            super(ref);
            this._mActivity=_mActivity;
        }

        @Override
        protected void weakHandleMessage(WalletRestorationDelegate ref, Message msg) {
            switch (msg.what) {
                case RESTORE_STATUS_UPDATE:
                    String workingOn = (String) msg.obj;
                    if (workingOn.isEmpty()) {
                        ref.status.setText(ref.getString(R.string.wallet_restoration_master_key));
                    } else {
                        ref.status.setText(ref.getString(R.string.wallet_restoration_coin, workingOn));
                    }
                    break;
                case RESTORE_FINISHED:
                 /*   WalletFromSeedTask task = walletFromSeedTask;
                    walletFromSeedTask = null;

                    if (task.wallet != null) {
                        //ref.popTo(SeedDelegate.class,true);
                        ref.startWithPop(WalletDelegate.getInstance());
                        // ref.getChildFragmentManager().popBackStack(null,1);

                        //ref.startWalletActivity();
                    } else {
                        String errorMessage = ref.getResources().getString(
                                R.string.wallet_restoration_error, task.errorMessage);
                        //ref.showErrorAndStartIntroActivity(errorMessage);
                    }*/
            }
        }
    }

  /*  public void startWalletActivity() {
        Intent intent = new Intent(getActivity(), WalletActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    private void showErrorAndStartIntroActivity(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        startActivity(new Intent(getActivity(), IntroActivity.class));
        getActivity().finish();
    }*/
}
