package kualian.dc.deal.application.ui.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;

/**
 * Created by idmin on 2018/2/24.
 */

public class CreateAccount extends SourceDelegate{
    public static CreateAccount getInstance(){
        return new CreateAccount();
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_create_wallet;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        TextView restore=(TextView) rootView.findViewById(R.id.account_restore);
        TextView create=(TextView) rootView.findViewById(R.id.account_create);
        setOnClickViews(restore,create);
    }

    public void onClick(View view){
        if (view.getId()==R.id.account_restore){
            start(SeedDelegate.getInstance(true));
        }else {
            start(SeedDelegate.getInstance(true));
        }
    }
}
