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
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.callback.OnCoinSelectListener;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by idmin on 2018/2/24.
 */

public class WalletCoinAdapter extends BaseQuickAdapter<CoinType, BaseViewHolder> {
    private Context context;

    public WalletCoinAdapter(int layoutResId, @Nullable List<CoinType> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CoinType item) {
        helper.setText(R.id.coin_name, item.getCoinName())
                .setText(R.id.coin_number, item.getCoinNum())
                ;
        ImageView imageView = helper.getView(R.id.coin_icon);
        //Glide.with(context).load(item.getCoinResource()).into(imageView);
        if (item.getCoinIndex() == 1) {
            imageView.setImageResource(R.drawable.coin_btc);
        } else {
            imageView.setImageResource(R.drawable.coin_ubtc);
        }
        //imageView.setImageResource(item.getCoinResource());
        helper.addOnClickListener(R.id.coin_contain);
        if (SpUtil.getInstance().getDefaultMoney()){
            helper.setText(R.id.coin_money, "≈$"+item.getUsMoney());
        }else {
            helper.setText(R.id.coin_money, "≈¥"+item.getCoinMoney());
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
