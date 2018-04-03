package kualian.dc.deal.application.ui.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.WordBean;
import kualian.dc.deal.application.ui.create.WalletLoginWord;
import kualian.dc.deal.application.ui.create.WalletName;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.widget.flowlayout.FlowLayout;
import kualian.dc.deal.application.widget.flowlayout.TagAdapter;
import kualian.dc.deal.application.widget.flowlayout.TagFlowLayout;

/**
 * Created by idmin on 2018/3/21.
 */

public class DelegateVerify extends SourceDelegate {
    private TagFlowLayout mSelect,mUnSelect;
    private ArrayList<WordBean> words = new ArrayList<>();
    private ArrayList<String> selectWords = new ArrayList<>();
    private ArrayList<String> original = new ArrayList<>();
    private int size;
    private String seed;
    private static final String TAG = "tag";
    private Button btn;
    private TextView back, error;
    private boolean isCompleted;
    private ArrayList<String> wordList;

    public static DelegateVerify getInstance(String seed) {
        DelegateVerify delegateVerify = new DelegateVerify();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, seed);
        delegateVerify.setArguments(bundle);
        return delegateVerify;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_verify;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        btn = rootView.findViewById(R.id.button_next);
        seed = getArguments().getString(TAG);
        back = rootView.findViewById(R.id.back);
        error = rootView.findViewById(R.id.restore_message);
        mUnSelect = rootView.findViewById(R.id.tag_un_select);
        mSelect = rootView.findViewById(R.id.tag_select);
        setOnClickViews(back, btn);
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        final LayoutInflater mInflater = LayoutInflater.from(getContext());
        wordList = KeyUtil.parseMnemonic(seed);
        original.addAll(wordList);
        shuffle(wordList);
        size = wordList.size();
        for (int i = 0; i < size; i++) {
            WordBean wordBean = new WordBean();
            wordBean.setContent(wordList.get(i));
            wordBean.setSelected(false);
            words.add(wordBean);
        }

        mSelect.setAdapter(new TagAdapter<String>(selectWords) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_word, mUnSelect, false);
                tv.setText(s);
                tv.setTextColor(getResources().getColor(R.color._6));
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                changeSelectItemState(position);
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                changeSelectItemState(position);
            }
        });
        mUnSelect.setAdapter(new TagAdapter<WordBean>(words) {
            @Override
            public View getView(FlowLayout parent, int position, WordBean s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_word, mUnSelect, false);
                tv.setText(s.getContent());
                if (s.isSelected()){
                    tv.setBackground(getResources().getDrawable(R.drawable.bg_blue_small));
                }else {
                    tv.setBackground(null);
                }
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                changeItemState(position,view);
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                changeItemState(position,view);

            }
        });
    }

    private void changeSelectItemState(int position) {
        int size = words.size();
        for (int i = 0; i < size; i++) {
            if (words.get(i).getContent().equals(selectWords.get(position))){
                words.get(i).setSelected(false);
                mUnSelect.getAdapter().notifyDataChanged();
                break;
            }
        }
        if (selectWords.size() == words.size()) {
            btn.setBackground(getResources().getDrawable(R.drawable.bg_white));
            btn.setTextColor(getResources().getColor(R.color.text_gray));
            error.setVisibility(View.GONE);
            isCompleted = false;
        }
        selectWords.remove(position);
        mSelect.getAdapter().notifyDataChanged();
    }

    private void changeItemState(int position,View view) {
        if (!words.get(position).isSelected()) {
            view.setBackground(getResources().getDrawable(R.drawable.bg_blue_small));
            words.get(position).setSelected(true);
            selectWords.add(wordList.get(position));

            mSelect.getAdapter().notifyDataChanged();
            if (selectWords.size() == words.size()) {
                isCompleted = true;
                btn.setBackground(getResources().getDrawable(R.drawable.bg_blue));
                btn.setTextColor(getResources().getColor(R.color.white));
            }
        }else {
            words.get(position).setSelected(false);
            view.setBackground(null);
            if (selectWords.size() == words.size()) {
                isCompleted = false;
                btn.setBackground(getResources().getDrawable(R.drawable.bg_white));
                btn.setTextColor(getResources().getColor(R.color.text_gray));
                error.setVisibility(View.GONE);
            }
            for (int i = 0; i < selectWords.size(); i++) {
                if (selectWords.get(i).equals(words.get(position).getContent())) {
                    selectWords.remove(i);
                    mSelect.getAdapter().notifyDataChanged();
                    break;
                }
            }
        }
    }

    public <T> void shuffle(List<T> list) {
        int size = list.size();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            // 获取随机位置
            int randomPos = random.nextInt(size);
            // 当前元素与随机元素交换
            Collections.swap(list, i, randomPos);
        }
    }

    private boolean verifyMnemonic() {
        boolean isSeedValid = true;
        for (int i = 0; i < size; i++) {
            if (!selectWords.get(i).equals(original.get(i))) {
                isSeedValid = false;
                setError(error, R.string.restore_error_checksum);
            }
        }

        return isSeedValid;
    }

    private void setError(TextView error, int restore_error_checksum) {
        error.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.back:
                _mActivity.onBackPressed();
                break;
            case R.id.button_next:
                if (isCompleted) {
                    if (verifyMnemonic()) {
                        start(WalletName.getInstance(seed));
                    }
                }
                break;
        }
    }
}
