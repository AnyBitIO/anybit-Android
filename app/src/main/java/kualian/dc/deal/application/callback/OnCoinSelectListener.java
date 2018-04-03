package kualian.dc.deal.application.callback;

import java.util.List;

import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by idmin on 2018/2/26.
 */

public interface OnCoinSelectListener {
    void onCoinSelect(List<CoinType> list);
}
