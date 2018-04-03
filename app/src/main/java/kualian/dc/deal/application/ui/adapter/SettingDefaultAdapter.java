package kualian.dc.deal.application.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Locale;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.callback.OnDefaultListener;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by idmin on 2018/2/24.
 */

public class SettingDefaultAdapter extends BaseQuickAdapter<CoinType, BaseViewHolder> {
    private int previous = 0, currentPosition = 0;
    private boolean isCoin;
    private OnDefaultListener onDefaultListener;

    public SettingDefaultAdapter(int layoutResId, @Nullable List<CoinType> data, boolean isCoin, OnDefaultListener onLanguageListener) {
        super(layoutResId, data);
        this.isCoin = isCoin;
        this.onDefaultListener = onLanguageListener;
        if (!isCoin) {
            if (SpUtil.getInstance().getDefaultLanguage() == null) {
                String language=Locale.getDefault().getLanguage();
                if (language.equals(Constants.LANGAE_ZH)) {
                    currentPosition = 0;
                } else {
                    currentPosition = 1;
                }
            } else {
                if (SpUtil.getInstance().getDefaultLanguage().equals(Constants.LANGAE_ZH)) {
                    currentPosition = 0;
                } else {
                    currentPosition = 1;
                }
            }

        }
    }

    @Override
    protected void convert(BaseViewHolder helper, final CoinType item) {
        if (helper.getAdapterPosition() == currentPosition) {
            helper.getView(R.id.select_item).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.select_item).setVisibility(View.GONE);
        }
        if (isCoin) {
            helper.setImageResource(R.id.coin_icon, item.getCoinResource());
        } else {
            helper.getView(R.id.coin_icon).setVisibility(View.GONE);
        }
        helper.setText(R.id.coin_name, item.getCoinName());
        helper.getView(R.id.coin_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.getView(R.id.select_item).setVisibility(View.VISIBLE);
                currentPosition = helper.getAdapterPosition();
                previous = currentPosition;
                if (isCoin) {
                    //SpUtil.getInstance().setDefaultCoinIndex(item.getCoinIndex());
                    onDefaultListener.getDefaultIndex(item.getCoinIndex());
                    notifyDataSetChanged();
                } else {
                    notifyItemRangeChanged(0, 2);
                    if (helper.getAdapterPosition() == 0) {
                        onDefaultListener.getLanguage(Constants.LANGAE_ZH);
                    } else {
                        onDefaultListener.getLanguage(Constants.LANGAE_EN);
                    }

                }
            }
        });
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
