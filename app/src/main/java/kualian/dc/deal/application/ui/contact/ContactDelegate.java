package kualian.dc.deal.application.ui.contact;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.ContactBean;
import kualian.dc.deal.application.bean.ContactQuery;
import kualian.dc.deal.application.bean.ContactResult;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.database.ContactDao;
import kualian.dc.deal.application.ui.adapter.ContactAdapter;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.widget.WaveSideBarView;

/**
 * Created by idmin on 2018/2/28.
 */

public class ContactDelegate extends SourceDelegate {
    private static final int REQUESTCODE = 10;
    private BaseMultiItemQuickAdapter mAdapter;
    private TextView clear;
    private static ContactDelegate instance;
    private List<ContactBean> mData = new ArrayList<>();
    private List<ContactBean> mNewData = new ArrayList<>();
    private RecyclerView mRecycler;
    private WaveSideBarView sideBarView;
    private AutoCompleteTextView input;
    private static final String TAG = "ContactDelegate";
    private ContactDao contactDao = new ContactDao();
    private LinearLayoutCompat searchContain;
    public static ContactDelegate getInstance() {
            instance = new ContactDelegate();
        return instance;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_contact;
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        clear = rootView.findViewById(R.id.clear);
        mRecycler = rootView.findViewById(R.id.recycler);
        sideBarView = rootView.findViewById(R.id.slide_bar);
        searchContain = rootView.findViewById(R.id.search_contain);
        findToolBar(rootView);
        toolTitle.setText(getResources().getString(R.string.contact));
        toolNext.setVisibility(View.VISIBLE);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_add_user);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        toolNext.setCompoundDrawables(drawable, null, null, null);
        input = rootView.findViewById(R.id.edit_input);
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        if (SpUtil.getInstance().getIsRestore()) {
            contactDao.deleteWithId(SpUtil.getInstance().getWalletID());
            queryAllContacts();
            SpUtil.getInstance().setIsRestore(false);
        } else {
            mData = contactDao.queryAll();
            if (mData.size()!=0){
                sideBarView.setVisibility(View.VISIBLE);
                searchContain.setVisibility(View.VISIBLE);
            }
            Collections.sort(mData);
            mNewData.clear();
            mNewData.addAll(mData);
        }
        mAdapter = new ContactAdapter(mData, false);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecycler);
        mAdapter.setEmptyView(R.layout.view_empty);
        //设置空视图
        TextView empty = mAdapter.getEmptyView().findViewById(R.id.empty_text);
        empty.setText(R.string.contact_no);
        ImageView emptyImage = mAdapter.getEmptyView().findViewById(R.id.empty_image);
        emptyImage.setImageResource(R.drawable.empty_contact);

        onInputListener();
        setOnClickViews(clear, toolBack);
        onAdapterListener();

        sideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int section = getPositionForSection(letter.charAt(0));
                mRecycler.scrollToPosition(section);
            }
        });
        /*//恢复钱包从服务器拉取联系人
        CallbackManager.getInstance().addCallback(CallbackType.ON_CONTACT, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {

            }
        });*/

        // queryAllContacts();

        CallbackManager.getInstance().addCallback(CallbackType.ON_REFRESH, new IGlobalCallback<String>() {
            @Override
            public void executeCallback(@Nullable String args) {
                List<ContactBean> contactBeans = contactDao.queryAll();
                mNewData.clear();
                mNewData.addAll(contactBeans);
                Collections.sort(contactBeans);
                if (contactBeans != null) {
                    mAdapter.getData().clear();
                    mAdapter.addData(contactBeans);
                }
                if (contactBeans.size()>0){
                    sideBarView.setVisibility(View.VISIBLE);
                    searchContain.setVisibility(View.VISIBLE);
                }
            }
        });
        //deleteContact();
    }

    private void onAdapterListener() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.right) {
                    ContactBean contactBean = (ContactBean) mAdapter.getData().get(position);
                    contactDao.delete(contactBean.getContactAddr());
                    deleteContact((ContactBean) mAdapter.getData().get(position));
                    mAdapter.getData().remove(position);
                    mAdapter.notifyItemChanged(position);
                    mAdapter.notifyItemRemoved(position);
                    if (mAdapter.getData().size()==0){
                        sideBarView.setVisibility(View.GONE);
                        searchContain.setVisibility(View.GONE);
                    }
                } else {
                    start(AddContact.getInstance((ContactBean) mAdapter.getData().get(position)));
                    CommonUtil.hideSoftInput(_mActivity);
                }
            }
        });
    }

    private void onInputListener() {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = input.getText().toString();
                if (content.length() > 0) {
                    clear.setVisibility(View.VISIBLE);
                    mAdapter.getData().clear();
                    mAdapter.addData(getNewList(content));
                } else {
                    clear.setVisibility(View.GONE);
                    mAdapter.getData().clear();
                    mAdapter.addData(mNewData);
                }
            }
        });
    }

    private void deleteContact(ContactBean dataBean) {
        ContactQuery contactQuery = new ContactQuery();
        ContactQuery.HeaderBean headerBean = new ContactQuery.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.delete);
        contactQuery.setHeader(headerBean);
        ContactQuery.BodyBean bodyBean = new ContactQuery.BodyBean(dataBean.getContactAddr(), dataBean.getNickName(), dataBean.getCoinType());
        contactQuery.setBody(bodyBean);
        presenter.queryServiceData(new Gson().toJson(contactQuery), Constants.delete);
    }

    private void queryAllContacts() {
        ContactQuery contactQuery = new ContactQuery();
        ContactQuery.HeaderBean headerBean = new ContactQuery.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.contacter_query);
        contactQuery.setHeader(headerBean);
        ContactQuery.BodyBean bodyBean = new ContactQuery.BodyBean("", "", "");
        contactQuery.setBody(bodyBean);
        presenter.queryServiceData(new Gson().toJson(contactQuery), Constants.contacter_query);
    }

    private List<ContactBean> getNewList(String content) {
        List<ContactBean> list = new ArrayList<>();
        for (ContactBean bean : mNewData) {
            if (bean.getNickName().contains(content) || bean.getContactAddr().contains(content)) {
                list.add(bean);
            }

        }
        return list;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.clear) {
            input.setText("");
        } else if (view.getId() == R.id.toolbar_back) {
            CommonUtil.hideSoftInput(_mActivity);
            _mActivity.onBackPressed();
        } else {
            start(new AddContact());
        }
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        int count = mData.size();
        for (int i = 0; i < count; i++) {
            char firstChar = mData.get(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String mResponse = response.getResponse();
                if (tag.equals(Constants.contacter_query)) {
                    ContactResult contactResult = new Gson().fromJson(mResponse, ContactResult.class);
                    if (contactResult != null && contactResult.getData() != null) {
                        List<ContactResult.DataBean.Contacter> dataBeans = contactResult.getData().getContacters();

                        mData.clear();
                        if (dataBeans == null) {
                            mAdapter.getData().clear();
                            mAdapter.addData(mData);
                            return;
                        }
                        for (ContactResult.DataBean.Contacter data :
                                dataBeans) {
                            String nickName = data.getNickName();
                            String coinType = data.getCoinType();
                            String contacterAddr = data.getContacterAddr();
                            ContactBean contact = new ContactBean();
                            contact.setNickName(nickName);
                            contact.setCoinType(coinType);
                            contact.setPinyin(nickName);
                            contact.setContactAddr(contacterAddr);
                            contactDao.add(nickName, contacterAddr, coinType,SpUtil.getInstance().getWalletID());
                            mData.add(contact);
                        }
                        if (dataBeans.size()>0){
                            sideBarView.setVisibility(View.VISIBLE);
                            searchContain.setVisibility(View.VISIBLE);
                        }


                        mNewData.clear();
                        mNewData.addAll(mData);
                        mAdapter.getData().clear();
                        mAdapter.addData(mNewData);
                        // SpUtil.getInstance().setIsRestore(false);
                    }

                }
            }
        });

    }
}
