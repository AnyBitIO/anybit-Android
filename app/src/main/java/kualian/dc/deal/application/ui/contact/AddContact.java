package kualian.dc.deal.application.ui.contact;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.ContactBean;
import kualian.dc.deal.application.bean.ContactHandle;
import kualian.dc.deal.application.bean.ContactResult;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.database.ContactDao;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.ui.camera.RequestCodes;
import kualian.dc.deal.application.ui.scan.ActivityScanerCode;
import kualian.dc.deal.application.util.BitcoinAddressValidator;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.widget.codeView.PayWindow;

/**
 * Created by idmin on 2018/3/1.
 */

public class AddContact extends SourceDelegate  {
    private static final String TAG = "AddContact";
    private TextView address, name, kind;
    private ImageView select;
    private boolean isUp;
    private LinearLayoutCompat mSelectContain;
    private ContactBean contactBean;
    private boolean isAdd = true;
    private ContactDao contactDao = new ContactDao();

    public static AddContact getInstance(ContactBean contactBean) {
        AddContact addContact = new AddContact();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, contactBean);
        addContact.setArguments(bundle);
        return addContact;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_contact_add;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);

        findToolBar(rootView);
        toolTitle.setText(getResources().getString(R.string.contact_add));
        ImageView scan = rootView.findViewById(R.id.scan_qr_code);
        address = rootView.findViewById(R.id.contact_address);
        name = rootView.findViewById(R.id.contact_nick);
        kind = rootView.findViewById(R.id.coin_kind);
        select = rootView.findViewById(R.id.coin_select);
        mSelectContain = rootView.findViewById(R.id.coin_select_contain);

        setOnClickViews(scan, mSelectContain);
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        if (getArguments().getSerializable(TAG) != null) {
            contactBean = (ContactBean) getArguments().getSerializable(TAG);
            if (contactBean != null) {
                isAdd = false;
                name.setText(contactBean.getNickName());
                address.setText(contactBean.getContactAddr());
                toolTitle.setText(R.string.contact_alter);
            }
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.toolbar_back:
                CommonUtil.hideSoftInput(_mActivity);
                _mActivity.onBackPressed();
                break;
            case R.id.coin_select_contain:
                /*if (isUp) {
                    select.setImageResource(R.drawable.ic_down);
                    isUp = false;
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                } else {
                    //popupWindow.showAsDropDown(select,  Gravity.CENTER_HORIZONTAL, 0, 0);
                    popupWindow.showAsDropDown(select, -CommonUtil.dip2px(200), 0);
                    select.setImageResource(R.drawable.ic_up);
                    isUp = true;
                }*/
                break;
            case R.id.toolbar_next:
                if (isAdd) {
                    addContact();
                } else {
                    editContact();
                }
//请求网络
                break;
            case R.id.scan_qr_code:
                startActivityForResult(new Intent(_mActivity, ActivityScanerCode.class), RequestCodes.SCAN);
                CallbackManager.getInstance()
                        .addCallback(CallbackType.ON_SCAN, new IGlobalCallback<String>() {
                            @Override
                            public void executeCallback(@Nullable String args) {
                                address.setText(args);
                            }
                        });
                break;
            default:
        }
    }

   /* private void createWindow() {
        popupWindow = new PopupWindow(getContext());
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.popup_layout, null);
        popupWindow.setContentView(inflate);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(CommonUtil.dip2px(160));
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setClippingEnabled(false); // 让PopupWindow同样覆盖状态栏
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent))); // 加上一层黑色透明背景

        LinearLayoutCompat bit = inflate.findViewById(R.id.bit);
        LinearLayoutCompat ub = inflate.findViewById(R.id.ub);

        bit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              *//*  select_bit.setVisibility(View.VISIBLE);
                select_ub.setVisibility(View.GONE);*//*
                icon.setImageResource(R.drawable.coin_btc);
                kind.setText("BTC");
                popupWindow.dismiss();
                select.setImageResource(R.drawable.ic_down);
                isUp = false;
            }
        });
        ub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               *//* select_bit.setVisibility(View.GONE);
                select_ub.setVisibility(View.VISIBLE);*//*
                icon.setImageResource(R.drawable.coin_ubtc);
                kind.setText("UBTC");
                popupWindow.dismiss();
                select.setImageResource(R.drawable.ic_down);
                isUp = false;
            }
        });
        //addContact();
        //editContact();

    }*/

    private void addContact() {
        if (!CommonUtil.stringFilter(name.getText().toString())){
            RxToast.showToast(R.string.tips_input_error);
            return;
        }
        if (name.getText().toString().length()>10){
            RxToast.showToast(R.string.tips_input_long);
            return;
        }
        if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(address.getText().toString()) || TextUtils.isEmpty(kind.getText().toString())) {
            RxToast.showToast(R.string.tips_finish);
            return;
        } else {
            if (!BitcoinAddressValidator.assertBitcoin(address.getText().toString().trim(), Constants.UB) && !CommonUtil.isNumeric(address.getText().toString().trim())) {
                RxToast.showToast(R.string.trade_address_error);
                return;
            }
            boolean add = contactDao.add(name.getText().toString(), address.getText().toString(), kind.getText().toString(), SpUtil.getInstance().getWalletID());
            AppLoader.showLoading(getContext());
            ContactHandle.HeaderBean header = new ContactHandle.HeaderBean(KeyUtil.getRandom());
            header.setTrancode(Constants.contacter_add);
            ContactHandle.BodyBean body = new ContactHandle.BodyBean(address.getText().toString(), kind.getText().toString(), name.getText().toString());
            ContactHandle contactHandle = new ContactHandle();
            contactHandle.setBody(body);
            contactHandle.setHeader(header);
            presenter.queryServiceData(new Gson().toJson(contactHandle), Constants.contacter_add);
            IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.ON_REFRESH);
            if (callback != null) {
                callback.executeCallback("");
            }
            AppLoader.stopLoading();
            CommonUtil.hideSoftInput(_mActivity);
            _mActivity.onBackPressed();

        }


    }

    private void editContact() {
        if (!CommonUtil.stringFilter(name.getText().toString())){
            RxToast.showToast(R.string.tips_input_error);
            return;
        }
        if (name.getText().toString().length()>10){
            RxToast.showToast(R.string.tips_input_long);
            return;
        }
        if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(address.getText().toString()) || TextUtils.isEmpty(kind.getText().toString())) {
            RxToast.showToast(R.string.tips_finish);
            return;
        } else {
            if (!BitcoinAddressValidator.assertBitcoin(address.getText().toString().trim(), Constants.UB) && !CommonUtil.isNumeric(address.getText().toString().trim())) {
                RxToast.showToast(R.string.trade_address_error);
                return;
            }
            contactDao.update(name.getText().toString(), address.getText().toString(), kind.getText().toString(), contactBean.getContactAddr());
            ContactHandle.HeaderBean header = new ContactHandle.HeaderBean(KeyUtil.getRandom());
            header.setTrancode(Constants.edit);
            ContactHandle.BodyBean body = new ContactHandle.BodyBean(address.getText().toString().trim(), kind.getText().toString(), name.getText().toString());
            body.setOrigContacterAddr(contactBean.getContactAddr());
            ContactHandle contactHandle = new ContactHandle();
            contactHandle.setBody(body);
            contactHandle.setHeader(header);
            presenter.queryServiceData(new Gson().toJson(contactHandle), Constants.edit);
            IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.ON_REFRESH);
            if (callback != null) {
                callback.executeCallback("");
            }
            AppLoader.stopLoading();
            CommonUtil.hideSoftInput(_mActivity);
            _mActivity.onBackPressed();
        }
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {

    }
}
