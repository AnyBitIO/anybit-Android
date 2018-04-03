package kualian.dc.deal.application.widget.codeView;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.callback.OnPwListener;
import kualian.dc.deal.application.util.CommonUtil;


public class PayWindow extends PopupWindow {
    private TextView ivClose;  // 关闭按钮
    private CodeView codeView;
    private LinearLayout contain;
    private boolean isSetting;//是否时设置密码界面
    private OnPwListener onPwListener;
     TextView title;
    public PayWindow(Context context, boolean isSetting) {
        this.isSetting = isSetting;
        init(context);
    }

    public PayWindow(Context context, boolean isSetting, OnPwListener onPwListener) {
        this.isSetting = isSetting;
        this.onPwListener = onPwListener;
        init(context);
    }

    private void init(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.window_code, null);
        setContentView(contentView);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setClippingEnabled(false); // 让PopupWindow同样覆盖状态栏
        setBackgroundDrawable(new ColorDrawable(0xAA000000)); // 加上一层黑色透明背景
        initView(contentView);
    }

    // 弹出PopupWindow
    public void show(View rootView) {
        showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, CommonUtil.getNavigationBarHeight(WalletApp.getContext()));
    }

   /* @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }*/
    private void initView(View contentView) {
        title = contentView.findViewById(R.id.wallet_pw_title);
        ivClose = contentView.findViewById(R.id.close_pass);
        if (isSetting) {
            final TextView textView = contentView.findViewById(R.id.wallet_pw_tip);
            textView.setVisibility(View.VISIBLE);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            contentView.findViewById(R.id.wallet_pw_tip).setVisibility(View.VISIBLE);
            ivClose.setVisibility(View.GONE);
            title.setText(R.string.account_pw);
        } else {
            title.setText(R.string.account_input_pw);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        final KeyboardView keyboardView = (KeyboardView) contentView.findViewById(R.id.password_input);
        codeView = (CodeView) contentView.findViewById(R.id.password_view);
        contain =  contentView.findViewById(R.id.code_contain);
        codeView.setShowType(CodeView.SHOW_TYPE_PASSWORD);
        codeView.setLength(6);
        keyboardView.setCodeView(codeView);
        codeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboardView.show();
            }
        });
        codeView.setListener(new CodeView.Listener() {
            @Override
            public void onValueChanged(String value) {
                // TODO: 2017/2/5  内容发生变化
            }

            @Override
            public void onComplete(String value) {
                if (onPwListener != null) {
                    onPwListener.getPw(value);
                }
                // TODO: 2017/2/5 输入完成
            }
        });
    }

    public CodeView getCodeView() {
        return codeView;
    }
    public LinearLayout getContainView() {
        return contain;
    }
}
