package kualian.dc.deal.application.ui.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.callback.OnCoinSelectListener;
import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by idmin on 2018/2/24.
 */

public class AddCoinAdapter extends BaseQuickAdapter<CoinType, BaseViewHolder> {
    private List<CoinType> mData;
    public AddCoinAdapter(int layoutResId, @Nullable List<CoinType> data) {
        super(layoutResId, data);
        this.mData=data;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CoinType item) {
        CoinType coin=mData.get(helper.getAdapterPosition());
        helper.setText(R.id.coin_name, coin.getCoinName());
        ImageView imageView = helper.getView(R.id.coin_kind);
        imageView.setImageResource(coin.getCoinResource());
        Switch sh = helper.getView(R.id.add_coin_switch);
        if (coin.getAddTag().equals(Constants.IS_ADD)){
            sh.setChecked(true);
        }else {
            sh.setChecked(false);
        }
        sh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    item.setAddTag(Constants.IS_ADD);
                }else {
                    item.setAddTag(Constants.IS_NO_ADD);
                }
            }
        });

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
