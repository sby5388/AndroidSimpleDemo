package com.shenby.launcher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class AppItemViewModel extends BaseObservable {

    private final PackageManager mPackageManager;
    private ResolveInfo mResolveInfo;

    public AppItemViewModel(PackageManager packageManager) {
        mPackageManager = packageManager;
    }

    public void setResolveInfo(ResolveInfo resolveInfo) {
        mResolveInfo = resolveInfo;
        notifyPropertyChanged(BR.title);
        notifyPropertyChanged(BR.logo);
    }

    @Bindable
    public String getTitle() {
        if (mResolveInfo == null) {
            return "";
        }
        return mResolveInfo.loadLabel(mPackageManager).toString();
    }

    @Bindable
    public Drawable getLogo() {
        if (mResolveInfo == null) {
            return null;
        }
        return mResolveInfo.loadIcon(mPackageManager);
    }

    public void onClick(View view) {
        if (mResolveInfo == null) {
            return;
        }
        final ActivityInfo info = mResolveInfo.activityInfo;
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(info.applicationInfo.packageName, info.name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final ResolveInfo result = mPackageManager.resolveActivity(intent, PackageManager.MATCH_ALL);
        if (result != null) {
            view.getContext().startActivity(intent);
        }
    }
}
