package kualian.dc.deal.application.callback;

import java.util.List;

public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermissions);
}