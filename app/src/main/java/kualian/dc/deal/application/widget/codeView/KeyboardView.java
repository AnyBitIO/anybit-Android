package kualian.dc.deal.application.widget.codeView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.RxToast;

/**
 *
 */
public class KeyboardView extends FrameLayout implements View.OnClickListener {

    private CodeView codeView;
    private Listener listener;

    public KeyboardView(Context context) {
        super(context);
        init();
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setCodeView(CodeView codeView) {
        this.codeView = codeView;
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_pwd_keyboard, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(view);
        view.findViewById(R.id.keyboard_0).setOnClickListener(this);
        view.findViewById(R.id.keyboard_1).setOnClickListener(this);
        view.findViewById(R.id.keyboard_2).setOnClickListener(this);
        view.findViewById(R.id.keyboard_3).setOnClickListener(this);
        view.findViewById(R.id.keyboard_4).setOnClickListener(this);
        view.findViewById(R.id.keyboard_5).setOnClickListener(this);
        view.findViewById(R.id.keyboard_6).setOnClickListener(this);
        view.findViewById(R.id.keyboard_7).setOnClickListener(this);
        view.findViewById(R.id.keyboard_8).setOnClickListener(this);
        view.findViewById(R.id.keyboard_9).setOnClickListener(this);
        view.findViewById(R.id.keyboard_hide).setOnClickListener(this);
        view.findViewById(R.id.keyboard_delete).setOnClickListener(this);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        final String tag = (String) v.getTag();
        if (tag != null) {
            switch (tag) {
                case "hide":
                    hide();
                    break;
                case "delete":
                    if (codeView != null) {
                        codeView.delete();
                    }
                    if (listener != null) {
                        listener.onDelete();
                    }
                    break;
                default:
                    if (codeView != null) {
                        codeView.input(tag);
                    }
                    if (listener != null) {
                        listener.onInput(tag);
                    }
                    break;
            }
        }
    }
    public void clear(){
        LogUtils.i("codeview "+codeView);

        if (codeView != null) {
            codeView.clear();
        }
    }
    public interface Listener {

        public void onInput(String s);

        public void onDelete();

    }

}
