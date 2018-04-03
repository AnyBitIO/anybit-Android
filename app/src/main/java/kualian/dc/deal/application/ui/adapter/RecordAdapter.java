package kualian.dc.deal.application.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.bean.RecordResponse;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by idmin on 2018/2/24.
 */

public class RecordAdapter extends BaseQuickAdapter<RecordResponse.DataBean.TradeBean, BaseViewHolder> {
    private static final String ONE = "1";
    private static final String TWO = "2";
    private Context context;

    public RecordAdapter(int layoutResId, @Nullable List<RecordResponse.DataBean.TradeBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final RecordResponse.DataBean.TradeBean item) {
        helper.setText(R.id.record_address, item.getTargetAddr())
                .setText(R.id.record_time, CommonUtil.stampToDate(item.getTranTime()));
        helper.addOnClickListener(R.id.contain);
        TextView state = helper.getView(R.id.record_state);
        TextView type = helper.getView(R.id.record_money);
        View back = helper.getView(R.id.record_item_back);
        if (item.getTranType().equals(ONE)) {
            type.setText("- " + item.getTranAmt());
            type.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            type.setText("+ " + item.getTranAmt());
            type.setTextColor(context.getResources().getColor(R.color.text_red));
        }
        if (item.getTranState().equals(ONE)) {
            state.setText(R.string.record_unsure);
            back.setBackgroundColor(context.getResources().getColor(R.color.text_red));
            state.setTextColor(context.getResources().getColor(R.color.text_red));
        } else if (item.getTranState().equals(TWO)) {
            back.setBackgroundColor(context.getResources().getColor(R.color.green));
            state.setText(R.string.record_success);
            state.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            state.setText(R.string.record_invalid);
            state.setTextColor(context.getResources().getColor(R.color.text_red));
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
