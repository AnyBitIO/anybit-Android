package kualian.dc.deal.application.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.callback.OnCoinSelectListener;
import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by idmin on 2018/2/24.
 */

public class CoinAdapter extends BaseQuickAdapter<CoinType, BaseViewHolder> {
    private List<CoinType> selectCoins = new ArrayList<>();
    private List<CoinType> saveCoins = new ArrayList<>();
    private OnCoinSelectListener mOnCoinSelectListener;
    private List<CoinType> mData;
    private boolean isAdd;
    public CoinAdapter(int layoutResId, @Nullable List<CoinType> data, OnCoinSelectListener onCoinSelectListener, boolean isAdd, CoinDao coinDao) {
        super(layoutResId, data);
        this.mData=data;
        this.mOnCoinSelectListener = onCoinSelectListener;
        this.isAdd = isAdd;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CoinType item) {
        CoinType coin=mData.get(helper.getAdapterPosition());
        helper.setText(R.id.coin_name, coin.getCoinName());
        ImageView imageView = helper.getView(R.id.coin_kind);
        imageView.setImageResource(coin.getCoinResource());
        Switch sh = helper.getView(R.id.add_coin_switch);
        if (isAdd){
            if (saveCoins!=null){
                for (CoinType coinType: saveCoins) {
                    if (coinType.getCoinName().equals(coin.getCoinName())){
                        sh.setChecked(true);
                    }
                }
            }

        }
        sh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!isAdd){
                    if (b) {
                        selectCoins.add(coin);
                    } else {
                        /*if (selectCoins.contains(coin)) {
                            selectCoins.remove(coin);
                        }*/
                        Iterator<CoinType> iterator = selectCoins.iterator();
                        while (iterator.hasNext()){
                            CoinType coinType=iterator.next();
                            if (coinType.getCoinName().equals(coin.getCoinName())){
                                iterator.remove();
                            }
                        }
                    }
                }else {
                    if (b) {
                        selectCoins.add(coin);
                    } else {
                        Iterator<CoinType> iterator = selectCoins.iterator();
                        while (iterator.hasNext()){
                            CoinType coinType=iterator.next();
                            if (coinType.getCoinName().equals(coin.getCoinName())){
                                iterator.remove();
                            }
                        }

                    }
                }
                if (mOnCoinSelectListener != null) {
                    mOnCoinSelectListener.onCoinSelect(selectCoins);
                }
            }
        });
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
