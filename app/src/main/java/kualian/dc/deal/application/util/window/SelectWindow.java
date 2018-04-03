package kualian.dc.deal.application.util.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.util.CommonUtil;

/**
 * Created by idmin on 2018/3/7.
 */

public class SelectWindow extends PopupWindow {
    private LinearLayoutCompat bit,ub;
    public SelectWindow(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.window_select, null);
        setContentView(inflate);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(CommonUtil.dip2px(160));
        setFocusable(false);
        setClippingEnabled(false); // 让PopupWindow同样覆盖状态栏
        setBackgroundDrawable(new ColorDrawable(0xAA000000)); // 加上一层黑色透明背景
        setOutsideTouchable(true);
        bit = inflate.findViewById(R.id.one);
        ub = inflate.findViewById(R.id.two);
    }
    public LinearLayoutCompat getOneView(){
        return bit;
    }
    public LinearLayoutCompat getTwoView(){
        return ub;
    }
}
