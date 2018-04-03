package kualian.dc.deal.application.bean;

import kualian.dc.deal.application.callback.CustomTabEntity;

/**
 * Created by idmin on 2018/2/10.
 */

public class TabEntity implements CustomTabEntity{
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;
    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }
    public TabEntity(String title) {
        this.title = title;
    }
    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
