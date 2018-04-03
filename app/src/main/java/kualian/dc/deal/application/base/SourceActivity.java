package kualian.dc.deal.application.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import java.util.Locale;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.callback.OnCloseDelegateListener;
import kualian.dc.deal.application.ui.MainDelegate;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.LanguageUtil;
import kualian.dc.deal.application.util.SpUtil;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by zheng on 2017/12/26.
 */

public class SourceActivity extends SupportActivity implements OnCloseDelegateListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this, true);
       // CommonUtil.switchLanguage(SpUtil.getInstance().getDefaultLanguage());
        setContentView(R.layout.container);
        if (findFragment(MainDelegate.class) == null) {
            loadRootFragment(R.id.fl_container, new MainDelegate());
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        String language=SpUtil.getInstance().getDefaultLanguage();
        if (language == null) {
            language=Locale.getDefault().getLanguage();
            if (!language.equals(Constants.LANGAE_ZH)){
                language=Constants.LANGAE_EN;
            }
        }
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, language));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void OnCloseDelegate() {
        startWithPop(MainDelegate.getInstance());

    }



    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }
}
