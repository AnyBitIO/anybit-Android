package kualian.dc.deal.application.ui.adapter;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Collections;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.bean.ContactBean;
import kualian.dc.deal.application.bean.ContactResult;
import kualian.dc.deal.application.presenter.impl.DataPresenter;
import kualian.dc.deal.application.wallet.coins.UbMain;

/**
 * Created by idmin on 2018/2/24.
 */
public class ContactAdapter extends BaseMultiItemQuickAdapter<ContactBean,BaseViewHolder> {
    private List<ContactBean> datas;
    private boolean isSelect;
    public ContactAdapter( List data,boolean isSelect) {
        super( data);
        this.datas=data;
        this.isSelect=isSelect;
        addItemType(ContactBean.CONTENT, R.layout.item_contact_content);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactBean item) {
    switch (helper.getItemViewType()){
        case ContactBean.CONTENT:
            helper.setText(R.id.contact_name,item.getNickName())
                    .setText(R.id.contact_coin_name,item.getCoinType())
                    .setText(R.id.contact_address,item.getContactAddr());
            helper.addOnClickListener(R.id.right);
            if (isSelect){
                helper.getView(R.id.right).setVisibility(View.GONE);
            }
            helper.addOnClickListener(R.id.content);
            TextView title=helper.getView(R.id.contact_title);
            //View line=helper.getView(R.id.line);
            //如果是第0个那么一定显示#号
            if (item.getCoinType().equals("BTC")){
                helper.setImageResource(R.id.contact_coin_icon,R.drawable.coin_btc);
            }else {
                helper.setImageResource(R.id.contact_coin_icon,R.drawable.coin_ubtc);

            }
            if (helper.getAdapterPosition() == 0) {
                title.setVisibility(View.VISIBLE);
                //helper.getView(R.id.content).setVisibility(View.GONE);
                //line.setVisibility(View.VISIBLE);
                //helper.getView(R.id.right).setVisibility(View.GONE);
                title.setText(item.getFirstChar()+"");
            } else {

                //如果和上一个item的首字母不同，则认为是新分类的开始
                ContactBean prevData = datas.get(helper.getAdapterPosition() - 1);
                if (item.getFirstChar() != prevData.getFirstChar()) {
                    title.setVisibility(View.VISIBLE);
                    title.setText("" + item.getFirstChar());
                   // line.setVisibility(View.VISIBLE);
                } else {
                    title.setVisibility(View.GONE);
                   // line.setVisibility(View.GONE);
                }
            }
            break;

    }
    }
}

